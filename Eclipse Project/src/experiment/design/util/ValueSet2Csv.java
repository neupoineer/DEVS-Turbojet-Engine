/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-16
 */
package experiment.design.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import experiment.toolkit.ExperimentState;
import experiment.toolkit.Settings;
import experiment.toolkit.SettingsSingleton;
import experiment.toolkit.Value;
import experiment.toolkit.ValueSet;

/**
 * The Class ValueSet2Csv.
 */
public class ValueSet2Csv {

  /** The settings. */
  private final Settings settings = SettingsSingleton.getInstance();

  /** The Constant defaultFilename. */
  private static final String defaultFilename = "DEVS_ExperimentEngine.csv";

  /** The Constant defaultCsvSeparator. */
  private static final String defaultCsvSeparator = ",";

  /** The Constant defaultCsvSeparator. */
  private static final String defaultCsvQuote = " ";

  /** The csv separator. */
  private char csvSeparator;

  /** The csv quote. */
  private char csvQuote;

  /** The filename. */
  private String filename;

  /** The header. */
  private boolean header;

  /** The writer. */
  private FileWriter writer;

  /**
   * Instantiates a new value set to CSV.
   */
  public ValueSet2Csv() {
    filename = settings.lookupString("CalibrationFile", defaultFilename);
    csvSeparator = settings.lookupChar("CsvSeparator", defaultCsvSeparator);
    csvQuote = settings.lookupChar("CsvQuote", defaultCsvQuote);
    header = true;

    final Path filePath = Paths.get(settings.getPathnameOutput(), filename);
    try {
      writer = new FileWriter(filePath.toString());
    } catch (IOException e) {
      System.err.println("ValueSet2Csv: Failure opening output file '" + filePath + "':  " + e.getMessage());
    }
  }

  /**
   * Process.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public void process(final ExperimentState experimentState, final ValueSet valueSet) {
    final List<String> list = new ArrayList<>();
    try {
      if (header) {
        list.add("Model");
        list.add("Run");
        list.add("Pass");
        list.add("Terminated");
        list.add("Completed");
        for (Entry<String, Value> entry : valueSet.getMap().entrySet()) {
          final Value value = entry.getValue();
          if (value.getReport()) {
            final String tmpName = StringUtils.isBlank(value.getAltName()) ? entry.getKey() : entry.getValue().getAltName();
            list.add(tmpName);
          }
        }
        writeLine(writer, list);
        writer.flush();
        list.clear();
        header = false;
      }

      // Write data rows
      list.add(Character.toString(experimentState.getModel()));
      list.add(Integer.toString(experimentState.getRun()));
      list.add(Long.toString(experimentState.getPass()));
      list.add(Boolean.toString(experimentState.isRunTerminated()));
      list.add(Boolean.toString(experimentState.isRunCompleted()));
      for (Entry<String, Value> entry : valueSet.getMap().entrySet()) {
        final Value value = entry.getValue();
        if (value.getReport())
          list.add(value.getValue().toString());
      }
      writeLine(writer, list);
      writer.flush();
    } catch (IOException e) {
      System.err.println("ValueSet2Csv: Failure writting header line to file: " + e.getMessage());
    }
  }

  /**
   * Write line.
   *
   * @param writer the writer
   * @param values the values
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void writeLine(final Writer writer, final List<String> values) throws IOException {
    boolean first = true;
    final StringBuilder sb = new StringBuilder();
    for (String value : values) {
      if (!first)
        sb.append(csvSeparator);
      if (csvQuote == ' ')
        sb.append(followCvsFormat(value));
      else
        sb.append(csvQuote).append(followCvsFormat(value)).append(csvQuote);
      first = false;
    }
    sb.append("\n");
    writer.append(sb.toString());
  }

  /**
   * Follow CSV format.
   *
   * @param value the value
   * @return the string
   */
  private String followCvsFormat(final String value) {
    // RFC 4180  https://tools.ietf.org/html/rfc4180
    String result = value;
    if (result.contains("\""))
      result = result.replace("\"", "\"\"");
    return result;
  }
}
