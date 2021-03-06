/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-08
 */
package TurbojetEngineMod.engine;

import experiment.toolkit.Calibration;
import experiment.toolkit.ExperimentState;
import experiment.toolkit.ValueSet;

/**
 * The Class EngineBase.
 */
public abstract class EngineBase {

  /** The name. */
  protected String name;

  /** The phase. */
  public String phase;

  /** The sigma. */
  public double sigma;

  /** The clock. */
  public double clock;

  /**
   * Instantiates a new engine base.
   *
   * @param name the name
   */
  public EngineBase(final String name) {
    this.name = name;
  }

  /**
   * Manifest model.
   *
   * @param calibration the calibration
   * @return true, if successful
   */
  public abstract boolean manifestModel(final Calibration calibration);

  /**
   * Process.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public abstract void process(final ExperimentState experimentState, final ValueSet valueSet);
}
