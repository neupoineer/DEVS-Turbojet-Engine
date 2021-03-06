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
 * The Class Compressor.
 */
public class Compressor extends EngineBase {

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /** Input: gamma. */
  private double gamma;

  /** Input: Cp. */
  private double Cp;

  /** Input: Tt2. */
  private double Tt2;

  /** Input: Pt2. */
  private double Pt2;

  /** Input: pi_c. */
  private double pi_c;

  /** Input: mdot_zero. */
  private double mdot_zero;

  /** Input: alpha. */
  private double alpha;

  /** Input: gamma_c. */
  private double gamma_c;

  /** Input: Cp_c. */
  private double Cp_c;

  /** Input: e_c. */
  private double e_c;

  /** Output: tao_c. */
  private double tao_c;

  /** Output: w_c. */
  private double w_c;

  /** Output: mdot_c. */
  private double mdot_c;

  /** Output: Wdot_c. */
  private double Wdot_c;

  /** Output: Pt3. */
  private double Pt3;

  /** Output: psi. */
  private double psi;

  /** Output: Tt3. */
  private double Tt3;

  /**
   * Instantiates a new compressor.
   *
   * @param name the name
   */
  public Compressor(final String name) {
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

    // Temp. Ratio @ station 3
    tao_c = Math.pow(pi_c,((gamma-1)/gamma));

    // Specific work, compressor
    w_c = Cp*(Tt2*(tao_c-1));  //[Btu/lbm]

    // Compressor Work
    // Wdot_c = mdot_zero*w_c;  //[Btu/s]

    // Compressor Work (in hp)
    Wdot_c = mdot_zero*w_c*3600*(1/2545.7);  //[hp]

    // Stagnation Pressure @ stage 3
    Pt3 = Pt2*pi_c;

    // Stagnation Pressure @ stage 3 (in psi)
    psi = Pt3/144;  //Pt3 in [psi]

    // Stagnation Temp. @ stage 3
    Tt3 = Tt2*tao_c;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_c", Value.theType.eDouble, tao_c, "", "Temp. Ratio @ station 3", "", true);
      valueSet.addValue(name, "w_c", Value.theType.eDouble, w_c, "[Btu/lbm]", "Specific work, compressor", "", true);
      valueSet.addValue(name, "Wdot_c", Value.theType.eDouble, Wdot_c, "[hp]", "Compressor Work", "", true);
      valueSet.addValue(name, "Pt3", Value.theType.eDouble, Pt3, "", "Stagnation Pressure @ stage 3", "", true);
      valueSet.addValue(name, "psi", Value.theType.eDouble, psi, "[psi]", "Stagnation Pressure @ stage 3 (in psi)", "", true);
      valueSet.addValue(name, "Tt3", Value.theType.eDouble, Tt3, "", "Stagnation Temp. @ stage 3", "", true);
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

    // Temp. Ratio @ station 3
    tao_c = Math.pow(pi_c,((gamma-1)/gamma));

    // Specific work, compressor
    w_c = Cp*(Tt2*(tao_c-1)); //[Btu/lbm]

    // mass flow rate through Compressor
    mdot_c = (1/(1+alpha))*mdot_zero;

    // Compressor Work
    // Wdot_c = mdot_c*w_c; //[Btu/s]

    // Compressor Work (in hp)
    Wdot_c = mdot_c*w_c*3600*(1/2545.7); //[hp]

    // Stagnation Pressure @ stage 3
    Pt3 = Pt2*pi_c;
    psi = Pt3/144; //Pt3 in [psi]

    // Stagnation Temp. @ stage 3
    Tt3 = Tt2*tao_c;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_c", Value.theType.eDouble, tao_c, "", "Temp. Ratio @ station 3", "", true);
      valueSet.addValue(name, "w_c", Value.theType.eDouble, w_c, "[Btu/lbm]", "Specific work, compressor", "", true);
      valueSet.addValue(name, "mdot_c", Value.theType.eDouble, mdot_c, "", "mass flow rate through Compressor", "", true);
      valueSet.addValue(name, "Wdot_c", Value.theType.eDouble, Wdot_c, "[hp]", "Compressor Work", "", true);
      valueSet.addValue(name, "Pt3", Value.theType.eDouble, Pt3, "[lbf/ft^2]", "Stagnation Pressure @ stage 3", "", true);
      valueSet.addValue(name, "psi", Value.theType.eDouble, psi, "[psi]", "Stagnation Pressure @ stage 3 (in psi)", "", true);
      valueSet.addValue(name, "Tt3", Value.theType.eDouble, Tt3, "[degR]", "Stagnation Temp. @ stage 3", "", true);
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

    // Temperature ratio at compressor
    tao_c = Math.pow(pi_c,((gamma_c-1)/(gamma_c*e_c)));

    // Compressor work
    w_c = Cp_c*(Tt2*(tao_c-1));  //[Btu/lbm]

    // Mass flow rate through compressor (core)
    mdot_c = (1/(1+alpha))*mdot_zero;

    // Compressor rate of work
    Wdot_c = mdot_c*w_c*3600*(1/2545.7);  //[hp]

    // Total Pressure at stage 3  [lbf/ft^2]
    Pt3 = Pt2*pi_c;
    psi = Pt3/144;  //Pt3 in [psi]

    // Total Temperature at stage 3 [degR]
    Tt3 = Tt2*tao_c;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_c", Value.theType.eDouble, tao_c, "", "Temp. Ratio @ station 3", "", true);
      valueSet.addValue(name, "w_c", Value.theType.eDouble, w_c, "[Btu/lbm]", "Specific work, compressor", "", true);
      valueSet.addValue(name, "mdot_c", Value.theType.eDouble, mdot_c, "", "mass flow rate through Compressor (core)", "", true);
      valueSet.addValue(name, "Wdot_c", Value.theType.eDouble, Wdot_c, "[hp]", "Compressor Work", "", true);
      valueSet.addValue(name, "Pt3", Value.theType.eDouble, Pt3, "[lbf/ft^2]", "Stagnation Pressure @ stage 3", "", true);
      valueSet.addValue(name, "psi", Value.theType.eDouble, psi, "[psi]", "Stagnation Pressure @ stage 3 (in psi)", "", true);
      valueSet.addValue(name, "Tt3", Value.theType.eDouble, Tt3, "[degR]", "Stagnation Temp. @ stage 3", "", true);
    }
    valueSet.valuePush(this);
  }
}
