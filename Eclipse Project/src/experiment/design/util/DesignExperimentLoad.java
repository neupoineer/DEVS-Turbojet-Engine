/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-17
 */
package experiment.design.util;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import experiment.toolkit.Settings;
import experiment.toolkit.SettingsSingleton;

/**
 * The Class DesignExperimentLoad.
 */
public class DesignExperimentLoad {

  /** The settings. */
  private final Settings settings = SettingsSingleton.getInstance();

  /** The Constant experimentFactors. */
  private static final String experimentFactors = "<factors>";

  /** The Constant experimentRuns. */
  private static final String experimentRuns = "<runs>";

  /** The Constant defaultFilename. */
  private static final String defaultFilename = "DesignExperiment.json";

  /** The pathname. */
  private String pathname;

  /** The filename. */
  private String filename;

  /**
   * Instantiates a new design experiment load.
   */
  public DesignExperimentLoad() {
    pathname = settings.lookupString("DesignExperimentPath", settings.getPathnameConfig());
    filename = settings.lookupString("DesignExperimentFile", defaultFilename);
  }

  /**
   * Load.
   *
   * @param designExperiment the design experiment
   */
  public void load(final DesignExperiment designExperiment) {
    final JSONParser parser = new JSONParser();
    final Path filePath = Paths.get(pathname, filename);
    try (FileReader fileReader = new FileReader(filePath.toFile())) {
      final Object obj = parser.parse(fileReader);
      final JSONObject jsonObject = (JSONObject) obj;
      for (final Object jsonKey : jsonObject.keySet()) {
        final String keyName = (String)jsonKey;
        final Object keyObject = jsonObject.get(keyName);
        switch (keyName) {
        case experimentFactors:
          loadFactors(designExperiment, keyObject);
          break;
        case experimentRuns:
          loadRuns(designExperiment, keyObject);
          break;
        default:
          throw new Exception("Element unsupported: " + keyName);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("DesignExperiment file " + filePath + ": " + e.getMessage());
    }
  }

  /**
   * Load factors.
   *
   * @param designExperiment the design experiment
   * @param keyObject the key object
   * @throws Exception the exception
   */
  private void loadFactors(final DesignExperiment designExperiment, final Object keyObject) throws Exception {
    if (keyObject instanceof JSONArray) {
      final JSONArray keyArray = (JSONArray)keyObject;
      for (int i = 0; i < keyArray.size(); i++) {
        final JSONObject factorArray = (JSONObject) keyArray.get(i);
        for (final Object factorKey : factorArray.keySet()) {
          final String factorDescription = (String)factorKey;
          final Object factorLevelObject = factorArray.get(factorKey);
          loadFactor(designExperiment, factorLevelObject, factorDescription);
        }
      }
    } else if (keyObject instanceof JSONObject)
      throw new Exception("Nested-object unsupported factor");
    else
      throw new Exception("Element unsupported factor");
  }

  /**
   * Load factor.
   *
   * @param designExperiment the design experiment
   * @param factorLevelObject the factor level object
   * @param factorDescription the factor description
   * @throws Exception the exception
   */
  private void loadFactor(final DesignExperiment designExperiment, final Object factorLevelObject, final String factorDescription) throws Exception {
    if (factorLevelObject instanceof JSONArray) {
      final JSONArray factoryLevelArray = (JSONArray)factorLevelObject;
      final DesignExperimentFactor experimentFactor = new DesignExperimentFactor(factorDescription);
      designExperiment.factorList.add(experimentFactor);
      for (int i = 0; i < factoryLevelArray.size(); i++) {
        if (factoryLevelArray.size() == 2) {
          final Object factorObject = factoryLevelArray.get(i);
          switch (i) {
          case 0:
            if (!(factorObject instanceof String))
              throw new Exception("Factor array index 0 must be a string: " + factorDescription);
            experimentFactor.setName((String)factorObject);
            break;
          default:
            if (!(factorObject instanceof JSONObject))
              throw new Exception("Factor array index " + i + " must be a Nested-object: " + factorDescription);
            final JSONObject levelObject = (JSONObject)factorObject;
            for (final Object levelKey : levelObject.keySet()) {
              final String levelName = (String)levelKey;
              final Object levelValue = levelObject.get(levelKey);
              if (!(levelValue instanceof String))
                throw new Exception("Factor level must be a string: " + factorDescription);
              experimentFactor.getLevels().put(levelName, (String)levelValue);
            }
            break;
          }
        } else
          throw new Exception("Factor array must have 2 entries: " + factorDescription);
      }
    } else if (factorLevelObject instanceof JSONObject)
      throw new Exception("Nested-object unsupported: " + factorDescription);
    else
      throw new Exception("Element unsupported: " + factorDescription);
  }

  /**
   * Load runs.
   *
   * @param designExperiment the design experiment
   * @param keyObject the key object
   * @throws Exception the exception
   */
  private void loadRuns(final DesignExperiment designExperiment, final Object keyObject) throws Exception {
    if (keyObject instanceof JSONArray) {
      final JSONArray keyArray = (JSONArray)keyObject;
      for (int i = 0; i < keyArray.size(); i++) {
        final JSONObject runArray = (JSONObject) keyArray.get(i);
        for (final Object runKey : runArray.keySet()) {
          final String runName = (String)runKey;
          final Object levelObject = runArray.get(runKey);
          loadRun(designExperiment, levelObject, runName);
        }
      }
    } else if (keyObject instanceof JSONObject)
      throw new Exception("Nested-object unsupported run");
    else
      throw new Exception("Element unsupported run");
  }

  /**
   * Load run.
   *
   * @param designExperiment the design experiment
   * @param levelObject the level object
   * @param runName the run name
   * @throws Exception the exception
   */
  private void loadRun(final DesignExperiment designExperiment, final Object levelObject, final String runName) throws Exception {
    if (levelObject instanceof JSONArray) {
      final JSONArray levelArray = (JSONArray)levelObject;
      final DesignExperimentRun experimentRun = new DesignExperimentRun(designExperiment.runList.size() + 1, runName, levelArray.size());
      designExperiment.runList.add(experimentRun);
      for (int i = 0; i < levelArray.size(); i++) {
        final Object entryObject = levelArray.get(i);
        if (!(entryObject instanceof String))
          throw new Exception("Run array entry must be a string: " + runName);
        final String level = (String)entryObject;
        experimentRun.getLevels()[i] = level;
        if (!designExperiment.factorList.get(i).getLevels().containsKey(level))
          throw new Exception("Run " + runName + " factor level " + i + " is not valid: " + level);
      }
    } else if (levelObject instanceof JSONObject)
      throw new Exception("Nested-object unsupported: " + runName);
    else
      throw new Exception("Element unsupported: " + runName);
  }

  /**
   * Gets the pathname.
   *
   * @return the pathname
   */
  public String getPathname() {
    return pathname;
  }

  /**
   * Gets the filename.
   *
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }
}
