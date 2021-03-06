/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-18
 */
package TurbojetEngineMod.engine;

import experiment.toolkit.Calibration;
import experiment.toolkit.ExperimentState;
import experiment.toolkit.Value;
import experiment.toolkit.ValueSet;

/**
 * The Class BypassFanNozzle.
 */
public class BypassFanNozzle extends EngineBase {

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /** The afterburner flag. */
  private final boolean KeTJET_Bool_Afterburner;

  /** Input: Tzero. */
  private double Tzero;

  /** Input: Pzero. */
  private double Pzero;

  /** Input: Mzero. */
  private double Mzero;

  /** Input: gamma. */
  private double gamma;

  /** Input: gc. */
  private double gc;

  /** Input: azero. */
  private double azero;

  /** Input: tao_r. */
  private double tao_r;

  /** Input: tao_fan. */
  private double tao_fan;

  /** Input: mdot_fan. */
  private double mdot_fan;

  /** Input: Pt8. */
  private double Pt8;

  /** Input: Tt8. */
  private double Tt8;

  /** The gamma_c. */
  private double gamma_c;

  /** The gamma_t. */
  private double gamma_t;

  /** The pi_r. */
  private double pi_r;

  /** The pi_d. */
  private double pi_d;

  /** The pi_fan. */
  private double pi_fan;

  /** Output: Pt9. */
  private double Pt9;

  /** Output: Tt9. */
  private double Tt9;

  /** Output: P9. */
  private double P9;

  /** Output: T9. */
  private double T9;

  /** Output: pi_fnz. */
  private double pi_fnz;

  /** Output: tao_fnz. */
  private double tao_fnz;

  /** Output: M_9_squared. */
  private double M_9_squared;

  /** Output: M_9. */
  private double M_9;

  /** Output: U_9_over_a_zero. */
  private double U_9_over_a_zero;

  /** Output: U_9_over_a_zero_squared. */
  private double U_9_over_a_zero_squared;

  /** Output: Fs_fan. */
  private double Fs_fan;

  /** Output: F_9. */
  private double F_9;

  /**
   * Instantiates a new bypass fan nozzle.
   *
   * @param name the name
   */
  public BypassFanNozzle(final String name) {
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

    // Total Press. @ station 9 - No losses
    Pt9 = Pt8;

    // Total Temp. @ station 9 - No losses
    Tt9 = Tt8;

    // Pressure @ Fan Nozzle Exit (to Ambient)
    P9 = Pzero; // [lbf/ft^2]

    // Temperature @ Fan Nozzle Exit (to Ambient)
    T9 = Tzero; // [degR]

    // Pressure Ratio @ station 9
    pi_fnz = Pt9/Pt8;

    // Temp. Ratio @ station 9
    tao_fnz = Tt9/Tt8;

    // Mach number at station 9 (M_9^2)
    M_9_squared = (2/(gamma-1))*((tao_r*tao_fan) - 1);

    M_9 = Math.sqrt(M_9_squared);

    // Velocity at station 9 wrt ambient speed of sound (U9/a0)^2
    // (Exactly the same as (M_9)^2, refer to proof)
    U_9_over_a_zero_squared = (2/(gamma-1))*((tao_r*tao_fan) - 1);

    // Fan (specific) Thrust [lbf/(lbm/s)]
    Fs_fan = (azero/gc)*((Math.sqrt( U_9_over_a_zero_squared))-Mzero);

    // Fan Thrust [lbf]
    F_9 = Fs_fan*mdot_fan;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "Pt9", Value.theType.eDouble, Pt9, "", "Total Press. @ station 9 - No losses", "", true);
      valueSet.addValue(name, "Tt9", Value.theType.eDouble, Tt9, "", "Total Temp. @ station 9 - No losses", "", true);
      valueSet.addValue(name, "P9", Value.theType.eDouble, P9, "", "Pressure @ Fan Nozzle Exit (to Ambient)", "", true);
      valueSet.addValue(name, "T9", Value.theType.eDouble, T9, "", "Temperature @ Fan Nozzle Exit (to Ambient)", "", true);
      valueSet.addValue(name, "pi_fnz", Value.theType.eDouble, pi_fnz, "", "Pressure Ratio @ station 9", "", true);
      valueSet.addValue(name, "tao_fnz", Value.theType.eDouble, tao_fnz, "", "Temp. Ratio @ station 9", "", true);
      valueSet.addValue(name, "M_9_squared", Value.theType.eDouble, M_9_squared, "", "Mach number at station 9", "(M_9)^2", true);
      valueSet.addValue(name, "M_9", Value.theType.eDouble, M_9, "", "", "", true);
      valueSet.addValue(name, "U_9_over_a_zero_squared", Value.theType.eDouble, U_9_over_a_zero_squared, "", "Velocity at station 9 wrt ambient speed of sound", "(U9/a0)^2", true);
      valueSet.addValue(name, "Fs_fan", Value.theType.eDouble, Fs_fan, "[lbf/(lbm/s)]", "Fan (specific) Thrust", "", true);
      valueSet.addValue(name, "F_9", Value.theType.eDouble, F_9, "[lbf]", "Fan Thrust", "", true);
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

    // Pressure at stage 9 [lbf/ft^2]
    P9 = Pzero/0.9;

    // Total Pressure at stage 9 [lbf/ft^2]
    Pt9 = Pzero*pi_r*pi_d*pi_fan*pi_fnz;

    // Temperature at stage 9 [degR]
    T9 = Tzero*((tao_r*tao_fan)/Math.pow((Pt9/P9),((gamma_c-1)/gamma_c)));

    // Total Temperature at stage 9  [degR]
    Tt9 = T9*Math.pow((Pt9/P9),((gamma_c-1)/gamma_c));

    // Temp. ratio at stage 9
    tao_fnz = Tt9/Tt8;

    // Mach at Fan Nozzle Exit
    M_9_squared = (2/(gamma_t-1))*(Math.pow((Pt9/P9),((gamma_c-1)/gamma_c)) - 1);
    M_9 = Math.sqrt(M_9_squared);

    // Fan Exhaust speed (ratio)
    U_9_over_a_zero_squared = (T9/Tzero)*M_9_squared;
    U_9_over_a_zero = Math.sqrt(U_9_over_a_zero_squared);

    // Fan Specific Thrust  [lbf/(lbm/s)]
    Fs_fan = (azero/gc)*( (U_9_over_a_zero) -Mzero+ ( ( (T9/Tzero)*(1-(Pzero/P9)) ) / ( (U_9_over_a_zero)*gamma_c) ) );

    // Fan Thrust [lbf]
    F_9 = Fs_fan*mdot_fan;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "P9", Value.theType.eDouble, P9, "", "Pressure @ Fan Nozzle Exit (to Ambient)", "", true);
      valueSet.addValue(name, "Pt9", Value.theType.eDouble, Pt9, "", "Total Press. @ station 9 - No losses", "", true);
      valueSet.addValue(name, "T9", Value.theType.eDouble, T9, "", "Temperature @ Fan Nozzle Exit (to Ambient)", "", true);
      valueSet.addValue(name, "Tt9", Value.theType.eDouble, Tt9, "", "Total Temp. @ station 9 - No losses", "", true);
      valueSet.addValue(name, "tao_fnz", Value.theType.eDouble, tao_fnz, "", "Temp. Ratio @ station 9", "", true);
      valueSet.addValue(name, "M_9", Value.theType.eDouble, M_9, "", "", "", true);
      valueSet.addValue(name, "M_9_squared", Value.theType.eDouble, M_9_squared, "", "Mach number at station 9", "(M_9)^2", true);
      valueSet.addValue(name, "U_9_over_a_zero", Value.theType.eDouble, U_9_over_a_zero, "", "Fan Exhaust speed (ratio)", "U9/a0", true);
      valueSet.addValue(name, "U_9_over_a_zero_squared", Value.theType.eDouble, U_9_over_a_zero_squared, "", "Velocity at station 9 wrt ambient speed of sound", "(U9/a0)^2", true);
      valueSet.addValue(name, "Fs_fan", Value.theType.eDouble, Fs_fan, "[lbf/(lbm/s)]", "Fan (specific) Thrust", "", true);
      valueSet.addValue(name, "F_9", Value.theType.eDouble, F_9, "[lbf]", "Fan Thrust", "", true);
    }
    valueSet.valuePush(this);
  }
}
