/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-03
 */
package TurbojetEngineMod.engine;

import experiment.toolkit.Calibration;
import experiment.toolkit.ExperimentState;
import experiment.toolkit.Value;
import experiment.toolkit.ValueSet;

/**
 * The Class Afterburner.
 */
public class Afterburner extends EngineBase {

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /** The afterburner flag. */
  private final boolean KeTJET_Bool_Afterburner;

  /** Input: Tt6. */
  private double Tt6;

  /** Input: Tzero. */
  private double Tzero;

  /** Input: Cp_c. */
  private double Cp_c;

  /** Input: Cp_t. */
  private double Cp_t;

  /** Input: afterburnerOn. */
  private Boolean afterburnerOn;

  /** Output: tao_lambda_ab. */
  private double tao_lambda_ab;

  /**
   * Instantiates a new afterburner.
   *
   * @param name the name
   */
  public Afterburner(final String name) {
    super(name);
    KeTJET_Str_FluidModel = "undefined";
    KeTJET_Bool_Afterburner = false;
  }

  /* (non-Javadoc)
   * @see TurbojetEngineMod.engine.EngineBase#manifestModel(experiment.toolkit.Calibration)
   */
  @Override
  public boolean manifestModel(final Calibration calibration) {
    calibration.update(this);
    return KeTJET_Bool_Afterburner;
  }

  /* (non-Javadoc)
   * @see TurbojetEngineMod.engine.EngineBase#process(experiment.toolkit.ExperimentState, experiment.toolkit.ValueSet)
   */
  public void process(final ExperimentState experimentState, final ValueSet valueSet) {
    switch (KeTJET_Str_FluidModel) {
    case "Isentropic/Static (subsonic)":
      IsentropicStaticSubsonic(experimentState, valueSet);
      break;
    case "Isentropic/Static (supersonic)":
      IsentropicStaticSupersonic(experimentState, valueSet);
      break;
    case "Polytropic/Static (supersonic)":
      PolytropicStaticSupersonic(experimentState, valueSet);
      break;
    default:
      System.out.println(name + "Undefined fluid model '" + KeTJET_Str_FluidModel + "'");
      break;
    }
  }

  /**
   * Isentropic static subsonic.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public void IsentropicStaticSubsonic(final ExperimentState experimentState, final ValueSet valueSet) {
  }

  /**
   * Isentropic static supersonic.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public void IsentropicStaticSupersonic(final ExperimentState experimentState, final ValueSet valueSet) {
    valueSet.valuePop(this);

    if (afterburnerOn)
      // Temperature ratio at Afterburner
      tao_lambda_ab = Tt6/Tzero;

    if (experimentState.isFirstPass()) {
      if (afterburnerOn)
        valueSet.addValue(name, "tao_lambda_ab", Value.theType.eDouble, tao_lambda_ab, "", "Temperature ratio at Afterburner", "", true);
    }
    valueSet.valuePush(this);
  }

  /**
   * Polytropic static supersonic.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public void PolytropicStaticSupersonic(final ExperimentState experimentState, final ValueSet valueSet) {
    valueSet.valuePop(this);

    if (afterburnerOn)
      // MAX Temperature ratio at Nozzle with Afterburner On
      tao_lambda_ab = (Tt6/Tzero)*(Cp_t/Cp_c);

    if (experimentState.isFirstPass()) {
      if (afterburnerOn)
        valueSet.addValue(name, "tao_lambda_ab", Value.theType.eDouble, tao_lambda_ab, "", "Temperature ratio at Afterburner", "", true);
    }
    valueSet.valuePush(this);
  }
}
