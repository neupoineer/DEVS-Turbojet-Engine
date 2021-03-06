/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-02
 */
package TurbojetEngineMod.engine;

import experiment.toolkit.Calibration;
import experiment.toolkit.ExperimentState;
import experiment.toolkit.Value;
import experiment.toolkit.ValueSet;

/**
 * The Class Diffuser.
 */
public class Diffuser extends EngineBase {

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /** Input: Tzero. */
  private double Tzero;

  /** Input: Pzero. */
  private double Pzero;

  /** Input: Mzero. */
  private double Mzero;

  /** Input: gamma. */
  private double gamma;

  /** Input: gamma_c. */
  private double gamma_c;

  /** Input: pi_d_max. */
  private double pi_d_max;

  /** Output: Tt0. */
  private double Tt0;

  /** Output: Tt1. */
  private double Tt1;

  /** Output: Tt2. */
  private double Tt2;

  /** Output: Pt0. */
  private double Pt0;

  /** Output: Pt1. */
  private double Pt1;

  /** Output: Pt2. */
  private double Pt2;

  /** Output: tao_r. */
  private double tao_r;

  /** Output: eta_r. */
  private double eta_r;

  /** Output: pi_r. */
  private double pi_r;

  /** Output: pi_d. */
  private double pi_d;

  /**
   * Instantiates a new diffuser.
   *
   * @param name the name
   */
  public Diffuser(final String name) {
    super(name);
    KeTJET_Str_FluidModel = "undefined";
  }

  /* (non-Javadoc)
   * @see TurbojetEngineMod.engine.EngineBase#manifestModel(experiment.toolkit.Calibration)
   */
  @Override
  public boolean manifestModel(final Calibration calibration) {
    calibration.update(this);
    return true;
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
    valueSet.valuePop(this);

    // Stagnation Temp. @ station 2
    Tt2 = Tzero*((1+((gamma-1)/2)*(Mzero*Mzero)));

    // Stagnation Press. @ station 2
    Pt2 = Pzero*Math.pow( ((1+((gamma-1)/2)*(Mzero*Mzero))),(gamma/(gamma-1)));

    // Temp. Ratio @ station 2
    tao_r = Tt2/Tzero;

    //Press. Ratio @ station 2
    pi_r = Pt2/Pzero;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "Tt2", Value.theType.eDouble, Tt2, "", "Stagnation Temp. @ station 2", "", true);
      valueSet.addValue(name, "Pt2", Value.theType.eDouble, Pt2, "", "Stagnation Press. @ station 2", "", true);
      valueSet.addValue(name, "tao_r", Value.theType.eDouble, tao_r, "", "Temp. Ratio @ station 2", "", true);
      valueSet.addValue(name, "pi_r", Value.theType.eDouble, pi_r, "", "Press. Ratio @ station 2", "", true);
    }
    valueSet.valuePush(this);
  }

  /**
   * Isentropic static supersonic.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public void IsentropicStaticSupersonic(final ExperimentState experimentState, final ValueSet valueSet) {
    valueSet.valuePop(this);

    // Stagnation Temp. @ station 2
    Tt2 = Tzero*((1+((gamma-1)/2)*(Mzero*Mzero)));

    // Stagnation Press. @ station 2
    Pt2 = Pzero*Math.pow( ((1+((gamma-1)/2)*(Mzero*Mzero))),(gamma/(gamma-1)));

    // Temp. Ratio @ station 2
    tao_r = Tt2/Tzero;

    // Press. Ratio @ station 2
    pi_r = Pt2/Pzero;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "Tt2", Value.theType.eDouble, Tt2, "", "Stagnation Temp. @ station 2", "", true);
      valueSet.addValue(name, "Pt2", Value.theType.eDouble, Pt2, "", "Stagnation Press. @ station 2", "", true);
      valueSet.addValue(name, "tao_r", Value.theType.eDouble, tao_r, "", "Temp. Ratio @ station 2", "", true);
      valueSet.addValue(name, "pi_r", Value.theType.eDouble, pi_r, "", "Press. Ratio @ station 2", "", true);
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

    // Stagnation Temp. @ station 0
    Tt0 = Tzero*(1+(((gamma_c-1)/2)*Math.pow(Mzero, 2)));  //[degR]

    // Stagnation Temp. @ station 1
    Tt1 = Tt0;  //[degR]

    // Stagnation Temp. @ station 2
    Tt2 = Tt0;  //[degR]

    // Stagnation Press. @ station 2
    Pt0 = Pzero*Math.pow( ((1+((gamma_c-1)/2)*(Mzero*Mzero))),(gamma_c/(gamma_c-1)));  //[lbf/ft^2]

    Pt1 = Pt0;  //[lbf/ft^2]
    Pt2 = Pt0;  //[lbf/ft^2]

    // Ram recurring factor
    if (Mzero < 1)
      eta_r = 1;
    else if (Mzero <= 5)
      eta_r = 1-(0.1*(Math.pow((Mzero-1), 1.5)));
    else
      eta_r = 800/((Math.pow(Mzero, 4))+9.35);

    // Press. Ratio @ station 2
    pi_d = pi_d_max*eta_r;

    // Temp. Ratio @ station 2
    tao_r = Tt0/Tzero;

    // Press. Ratio @ station 2
    pi_r = Math.pow(tao_r, (gamma_c/(gamma_c-1)));

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "Tt0", Value.theType.eDouble, Tt0, "", "Stagnation Temp. @ station 0", "", true);
      valueSet.addValue(name, "Tt1", Value.theType.eDouble, Tt1, "", "Stagnation Temp. @ station 1", "", true);
      valueSet.addValue(name, "Tt2", Value.theType.eDouble, Tt2, "", "Stagnation Temp. @ station 2", "", true);
      valueSet.addValue(name, "Pt0", Value.theType.eDouble, Pt0, "", "Stagnation Press. @ station 0", "", true);
      valueSet.addValue(name, "Pt1", Value.theType.eDouble, Pt1, "", "Stagnation Press. @ station 1", "", true);
      valueSet.addValue(name, "Pt2", Value.theType.eDouble, Pt2, "", "Stagnation Press. @ station 2", "", true);
      valueSet.addValue(name, "eta_r", Value.theType.eDouble, eta_r, "", "Ram recurring factor", "", true);
      valueSet.addValue(name, "tao_r", Value.theType.eDouble, tao_r, "", "Temp. Ratio @ station 2", "", true);
      valueSet.addValue(name, "pi_r", Value.theType.eDouble, pi_r, "", "Press. Ratio @ station 2", "", true);
      valueSet.addValue(name, "pi_d", Value.theType.eDouble, pi_d, "", "Press. Ratio @ station 2", "", true);
    }
    valueSet.valuePush(this);
  }
}
