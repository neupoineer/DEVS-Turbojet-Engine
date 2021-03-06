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
 * The Class Combustor.
 */
public class Combustor extends EngineBase {

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /** Input: Tzero. */
  private double Tzero;

  /** Input: Cp. */
  private double Cp;

  /** Input: tao_r. */
  private double tao_r;

  /** Input: tao_c. */
  private double tao_c;

  /** Input: Pt3. */
  private double Pt3;

  /** Input: hpr. */
  private double hpr;

  /** Input: Tt3. */
  private double Tt3;

  /** Input: Tt4. */
  private double Tt4;

  /** Input: Cp_c. */
  private double Cp_c;

  /** Input: Cp_t. */
  private double Cp_t;

  /** Output: tao_lambda. */
  private double tao_lambda;

  /** Output: Pt4. */
  private double Pt4;

  /** Output: pi_b. */
  private double pi_b;

  /** Output: f. */
  private double f;

  /** Output: tao_b. */
  private double tao_b;

  /**
   * Instantiates a new combustor.
   *
   * @param name the name
   */
  public Combustor(final String name) {
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

    // Temp. Ratio @ station 4 inlet (station 3 outlet)
    tao_lambda = Tt4/Tzero;  //(Turbine Inlet Temp./Inlet Temp.)

    // Static Pressure @ station 4 inlet Pt3=Pt4
    Pt4=Pt3;  //[Btu/lbm]

    // Pressure ratio at burner (combustor)
    pi_b = Pt4/Pt3;

    // fuel flow (ratio)
    f = ((Cp*Tzero)/hpr)*(tao_lambda-(tao_c*tao_r));

    // Temp. ratio at burner (combustor)
    tao_b = (((f*hpr)/(Cp*Tzero))*(1/(tao_c*tao_r))+1);

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_lambda", Value.theType.eDouble, tao_lambda, "", "Temp. Ratio @ station 4 inlet (station 3 outlet)", "", true);
      valueSet.addValue(name, "Pt4", Value.theType.eDouble, Pt4, "[Btu/lbm]", "Static Pressure @ station 4 inlet Pt3=Pt4", "", true);
      valueSet.addValue(name, "pi_b", Value.theType.eDouble, pi_b, "", "Pressure ratio at burner (combustor)", "", true);
      valueSet.addValue(name, "f", Value.theType.eDouble, f, "", "fuel flow (ratio)", "", true);
      valueSet.addValue(name, "tao_b", Value.theType.eDouble, tao_b, "", "Temp. ratio at burner (combustor)", "", true);
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

    // Temp. Ratio @ station 4 inlet (station 3 outlet)
    tao_lambda = Tt4/Tzero;  //(Turbine Inlet Temp./Inlet Temp.)

    // Static Pressure @ station 4 inlet Pt3=Pt4
    Pt4=Pt3;  //[Btu/lbm]

    // Pressure ratio at burner (combustor)
    pi_b = Pt4/Pt3;

    // Temp. ratio at burner (combustor)
    // tao_b = (((f*hpr)/(Cp*Tzero))*(1/(tao_c*tao_r))+1);
    tao_b = Tt4/Tt3;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_lambda", Value.theType.eDouble, tao_lambda, "", "Temp. Ratio @ station 4 inlet (station 3 outlet)", "", true);
      valueSet.addValue(name, "Pt4", Value.theType.eDouble, Pt4, "[Btu/lbm]", "Static Pressure @ station 4 inlet Pt3=Pt4", "", true);
      valueSet.addValue(name, "pi_b", Value.theType.eDouble, pi_b, "", "Pressure ratio at burner (combustor)", "", true);
      valueSet.addValue(name, "tao_b", Value.theType.eDouble, tao_b, "", "Temp. ratio at burner (combustor)", "", true);
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

    // Max Temperature ratio at Turbine Inlet
    tao_lambda = (Tt4/Tzero)*(Cp_t/Cp_c);  //(Turbine Inlet Temp./Inlet Temp.)

    // Total Pressure at station 4
    Pt4=Pt3*pi_b;  //[lbf/ft^2]

    // Temperature ratio at burner (Combustor)
    tao_b = (Cp_c/Cp_t)*(tao_lambda/(tao_c*tao_r));

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "tao_lambda", Value.theType.eDouble, tao_lambda, "", "Temp. Ratio @ station 4 inlet (station 3 outlet)", "", true);
      valueSet.addValue(name, "Pt4", Value.theType.eDouble, Pt4, "[Btu/lbm]", "Static Pressure @ station 4 inlet Pt3=Pt4", "", true);
      valueSet.addValue(name, "tao_b", Value.theType.eDouble, tao_b, "", "Temp. ratio at burner (combustor)", "", true);
    }
    valueSet.valuePush(this);
  }
}
