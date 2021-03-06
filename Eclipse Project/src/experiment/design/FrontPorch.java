/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-09
 */
package experiment.design;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.io.FilenameUtils;

import experiment.design.util.DesignExperiment;
import experiment.design.util.DesignExperimentLoad;
import experiment.design.util.DesignExperimentRun;
import experiment.model.ExperimentEngineFrontPorch;
import experiment.toolkit.ExperimentState;
import experiment.toolkit.Value;
import experiment.toolkit.ValueSet;
import experiment.toolkit.ValueSetLoad;
import experiment.toolkit.ValueSetPair;

/**
 * The Class FrontPorch.
 */
public class FrontPorch extends ExperimentEngineFrontPorch {

  /** The design experiment. */
  private DesignExperiment designExperiment;

  /** The dump design experiment. */
  private final boolean dumpDesignExperiment;

  /** The dump message alignment. */
  private final int dumpMessageAlignment;

  /** The model count. */
  private final int modelCount;

  /** The model experiment. */
  private final int[] modelExperiment;

  /**
   * Instantiates a new front porch.
   *
   * @param name the name
   * @param showModelState the show model state
   * @param colorName the color name
   * @param dumpDesignExperiment the dump design experiment
   * @param dumpMessageAlignment the dump message alignment
   * @param modelCount the model count
   */
  public FrontPorch(final String name, final boolean showModelState, final String colorName, final boolean dumpDesignExperiment, final int dumpMessageAlignment, final int modelCount) {
    super(name, showModelState, colorName);
    this.dumpDesignExperiment = dumpDesignExperiment;
    this.dumpMessageAlignment = dumpMessageAlignment;
    this.modelCount = modelCount;
    modelExperiment = new int[modelCount];
    loadExperiment();
  }

  /**
   * Initialize.
   */
  @Override
  public void initialize() {
    super.initialize();

    for (int i = 0; i < modelCount; i++)
      modelExperiment[i] = 0;
  }

  /**
   * Load experiment.
   */
  private void loadExperiment() {
    designExperiment = new DesignExperiment();
    final DesignExperimentLoad designExperimentLoad = new DesignExperimentLoad();
    designExperimentLoad.load(designExperiment);
    if (dumpDesignExperiment)
      designExperiment.dump();
  }

  /**
   * Prepare experiment.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  private void prepareExperiment(final ExperimentState experimentState, final ValueSet valueSet) {
    final DesignExperimentRun experimentRun = designExperiment.runList.get(experimentState.getRun() - 1);
    for (int i = 0; i < experimentRun.getLevels().length; i++) {
      final String name = designExperiment.factorList.get(i).getName();
      final String level = experimentRun.getLevels()[i];
      final String newValue = designExperiment.factorList.get(i).getLevels().get(level);
      if (valueSet.getMap().containsKey(name)) {
        final Value valueObj = valueSet.getMap().get(name);
        valueObj.setValueFromString(newValue);
      }
    }
  }

  /* (non-Javadoc)
   * @see experiment.model.ExperimentEngineFrontPorchInterface#startExperiments(java.util.Queue)
   */
  @Override
  public void startExperiments(final Queue<ValueSetPair> valueSetPairQueueOut) {
    for (int i = 0; i < modelCount; i++)
      if (modelExperiment[i] == 0) {
        modelExperiment[i] += 1;
        final char model = (char) ('A' + i);
        final ExperimentState experimentState = new ExperimentState(model, modelExperiment[i]);
        final ValueSet valueSet = createValueSet(model);
        prepareExperiment(experimentState, valueSet);
        final ValueSetPair valueSetPair = new ValueSetPair(experimentState, valueSet);
        valueSetPairQueueOut.add(valueSetPair);
      }
  }

  /* (non-Javadoc)
   * @see experiment.model.ExperimentEngineFrontPorchInterface#processExperiments(java.util.Queue)
   */
  @Override
  public void processExperiments(final Queue<ValueSetPair> valueSetPairQueueIn) {
    if (valueSetPairQueueIn.size() > 0) {
      // Separate finished runs from continuing runs
      final Queue<ValueSetPair> valueSetPairFinishedQueue = new LinkedList<ValueSetPair>();
      for (final ValueSetPair valueSetPair : valueSetPairQueueIn)
        if (valueSetPair.isRunTerminated() || valueSetPair.isRunCompleted())
          valueSetPairFinishedQueue.add(valueSetPair);
      for (final ValueSetPair valueSetPairFinished : valueSetPairFinishedQueue) {
        valueSetPairQueueIn.remove(valueSetPairFinished);

        System.out.println(name + ": " + "Experiment run " + (valueSetPairFinished.isRunCompleted() ? "completed" : "terminated") + ": " + valueSetPairFinished.toString());

        // Start next run
        modelExperiment[valueSetPairFinished.getModelIndex()] += 1;
        if (modelExperiment[valueSetPairFinished.getModelIndex()] <= designExperiment.getRunList().size()) {
          final char model = (char) ('A' + valueSetPairFinished.getModelIndex());
          final ExperimentState experimentState = new ExperimentState(model, modelExperiment[valueSetPairFinished.getModelIndex()]);
          final ValueSet valueSet = createValueSet(model);
          prepareExperiment(experimentState, valueSet);
          final ValueSetPair valueSetPair = new ValueSetPair(experimentState, valueSet);
          valueSetPairQueueIn.add(valueSetPair);
        }
      }
    }
  }

  /**
   * Creates the value set.
   *
   * @param model the model
   * @return the value set
   */
  private ValueSet createValueSet(final char model) {
    final ValueSet valueSet = new ValueSet();
    valueSet.setDumpMessageAlignment(dumpMessageAlignment);
    final ValueSetLoad valueSetLoad = new ValueSetLoad();
    final String valueSetFilename = FilenameUtils.getBaseName(valueSetLoad.getFilename()) + model + '.' + FilenameUtils.getExtension(valueSetLoad.getFilename());
    valueSetLoad.load(valueSet, valueSetFilename);
    return valueSet;
  }

  /**
   * Are runs complete.
   *
   * @return true, if successful
   */
  public boolean areRunsComplete() {
    final int totalRuns = designExperiment.getRunList().size();
    int minRuns = totalRuns;
    for (int i = 0; i < modelCount; i++)
      if (modelExperiment[i] < minRuns)
        minRuns = modelExperiment[i];
    return minRuns >= totalRuns;
  }

  /**
   * Gets the model count.
   *
   * @return the model count
   */
  public int getModelCount() {
    return modelCount;
  }
}
