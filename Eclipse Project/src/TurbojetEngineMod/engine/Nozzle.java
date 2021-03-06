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
 * The Class Nozzle.
 */
public class Nozzle extends EngineBase {

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

  /** Input: gamma_t. */
  private double gamma_t;

  /** Input: gc. */
  private double gc;

  /** Input: azero. */
  private double azero;

  /** Input: tao_r. */
  private double tao_r;

  /** Input: mdot_zero. */
  private double mdot_zero;

  /** Input: tao_c. */
  private double tao_c;

  /** Input: tao_lambda. */
  private double tao_lambda;

  /** Input: f. */
  private double f;

  /** Input: tao_t. */
  private double tao_t;

  /** Input: Tt5. */
  private double Tt5;

  /** Input: Tt6. */
  private double Tt6;

  /** Input: Pt5. */
  private double Pt5;

  /** Input: P9. */
  private double P9;

  /** Input: T9. */
  private double T9;

  /** Input: Cp. */
  private double Cp;

  /** Input: Cp_c. */
  private double Cp_c;

  /** Input: Cp_t. */
  private double Cp_t;

  /** Input: hpr. */
  private double hpr;

  /** Input: tao_lambda_ab. */
  private double tao_lambda_ab;

  /** Input: U_9_over_a_zero. */
  private double U_9_over_a_zero;

  /** Input: U_9_over_a_zero_squared. */
  private double U_9_over_a_zero_squared;

  /** Input: pi_c. */
  private double pi_c;

  /** Input: pi_d. */
  private double pi_d;

  /** Input: pi_t. */
  private double pi_t;

  /** Input: alpha. */
  private double alpha;

  /** Input: Rair_c. */
  private double Rair_c;

  /** Input: Rair_t. */
  private double Rair_t;

  /** Input: afterburnerOn. */
  private Boolean afterburnerOn;

  /**  Input: Pzero_over_P7. */
  private double Pzero_over_P7;

  /** Output: p7. */
  private double P7;

  /** Output: t7. */
  private double T7;

  /** Output: Pt7. */
  private double Pt7;

  /** Output: Tt7. */
  private double Tt7;

  /** Output: pi_b. */
  private double pi_b;

  /** Output: pi_n. */
  private double pi_n;

  /** Output: pi_r. */
  private double pi_r;

  /** Output: tao_n. */
  private double tao_n;

  /** Output: m_7. */
  private double M_7;

  /** Output: U_7_over_a_7. */
  private double U_7_over_a_7;

  /** Output: U_7_over_a_zero. */
  private double U_7_over_a_zero;

  /** Output: U_7_over_a_zero_squared. */
  private double U_7_over_a_zero_squared;

  /** Output: F_s. */
  private double F_s;

  /** Output: f_7. */
  private double F_7;

  /** Output: tsfc. */
  private double TSFC;

  /** Output: eta_t. */
  private double eta_t;

  /** Output: eta_p. */
  private double eta_p;

  /** Output: eta_o. */
  private double eta_o;

  /** Output: f_ab. */
  private double f_ab;

  /** Output: Tt7_over_Tt_5. */
  private double Tt7_over_Tt5;

  /** Output: U_7_ab_over_a_zero. */
  private double U_7_ab_over_a_zero;

  /** Output: U_7_ab_over_a_zero_squared. */
  private double U_7_ab_over_a_zero_squared;

  /** Output: U_7_ab. */
  private double U_7_ab;

  /** Output: U_0. */
  private double U_0;

  /** Output: U_9. */
  private double U_9;

  /** Output: F_s_c_ab. */
  private double F_s_c_ab;

  /** Output: F_s_ab. */
  private double F_s_ab;

  /** Output: F_s_c. */
  private double F_s_c;

  /** Output: F_s_fan. */
  private double F_s_fan;

  /** Output: F. */
  private double F;

  /** Output: F_ab. */
  private double F_ab;

  /** Output: F_7_ab. */
  private double F_7_ab;

  /** Output: TSF_C_ab. */
  private double TSFC_ab;

  /** Output: eta_t_ab. */
  private double eta_t_ab;

  /** Output: eta_p_ab. */
  private double eta_p_ab;

  /** Output: eta_o_ab. */
  private double eta_o_ab;

  /**
   * Instantiates a new nozzle.
   *
   * @param name the name
   */
  public Nozzle(final String name) {
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

    // Expanded to Ambient Pressure and Temperature
    P7 = Pzero;  //[lbf/ft^2]
    T7 = (tao_lambda/(tao_r*tao_c))*Tzero; //[degR]

    // No loss in Static Pressure or Static Temperature (Isentropic)
    Pt7 = Pt5;  //[lbf/ft^2]
    Tt7 = Tt5;  //[degR]

    // Pressure ratio at Nozzle
    pi_n = Pt7/Pt5;

    // Temp. ratio at Nozzle
    tao_n = Tt7/Tt5;

    // Mach number at Nozzle Exit
    M_7 = Math.sqrt((2/(gamma-1))*((tao_r*tao_c*tao_t)-1));

    // Velocity ratio at Nozzle exit
    U_7_over_a_7 = M_7;

    // Velocity ratio over Ambient speed of sound
    U_7_over_a_zero = Math.sqrt((2/(gamma-1))*(tao_lambda/(tao_r*tao_c))*((tao_r*tao_c*tao_t)-1));

    // Specific Thrust
    F_s = (azero/gc)*(U_7_over_a_zero-Mzero);  //[lbf/(lbm/s)]

    // Thrust
    F_7 = F_s*mdot_zero;  //[lbf]

    // Thrust Specific Fuel Consumption (TSFC)
    TSFC = (f/F_s)*3600;  //[(lbm/hr)/lbf] or [1/hr]

    // Thermal Efficiency of Turbojet Engine
    eta_t = 1-(1/(tao_r*tao_c));

    // Performance Efficiency of Turbojet Engine
    eta_p = (2*Mzero)/(U_7_over_a_zero+Mzero);

    // Overall Turbojet Engine Efficiency
    eta_o = eta_t*eta_p;

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "P7", Value.theType.eDouble, P7, "[lbf/ft^2]", "", "", true);
      valueSet.addValue(name, "T7", Value.theType.eDouble, T7, "[degR]", "", "", true);
      valueSet.addValue(name, "Pt7", Value.theType.eDouble, Pt7, "[lbf/ft^2]", "", "", true);
      valueSet.addValue(name, "Tt7", Value.theType.eDouble, Tt7, "[degR]", "", "", true);
      valueSet.addValue(name, "pi_n", Value.theType.eDouble, pi_n, "", "Pressure ratio at Nozzle", "", true);
      valueSet.addValue(name, "tao_n", Value.theType.eDouble, tao_n, "", "Temp. ratio at Nozzle", "", true);
      valueSet.addValue(name, "M_7", Value.theType.eDouble, M_7, "", "Mach number at Nozzle Exit", "", true);
      valueSet.addValue(name, "U_7_over_a_7", Value.theType.eDouble, U_7_over_a_7, "", "Velocity ratio at Nozzle exit", "U7/a7", true);
      valueSet.addValue(name, "U_7_over_a_zero", Value.theType.eDouble, U_7_over_a_zero, "", "Velocity ratio over Ambient speed of sound", "U7/a0", true);
      valueSet.addValue(name, "F_s", Value.theType.eDouble, F_s, "[lbf/(lbm/s)]", "Specific Thrust", "", true);
      valueSet.addValue(name, "F_7", Value.theType.eDouble, F_7, "[lbf]", "Thrust", "", true);
      valueSet.addValue(name, "TSFC", Value.theType.eDouble, TSFC, "[(lbm/hr)/lbf] or [1/hr]", "Thrust Specific Fuel Consumption (TSFC)", "", true);
      valueSet.addValue(name, "eta_t", Value.theType.eDouble, eta_t, "", "Thermal Efficiency of Turbojet Engine", "", true);
      valueSet.addValue(name, "eta_p", Value.theType.eDouble, eta_p, "", "Performance Efficiency of Turbojet Engine", "", true);
      valueSet.addValue(name, "eta_o", Value.theType.eDouble, eta_o, "", "Overall Turbojet Engine Efficiency", "", true);
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

    // Expanded to Ambient Pressure and Temperature
    P7 = Pzero;  //[lbf/ft^2]
    T7 = (tao_lambda/(tao_r*tao_c))*Tzero; //[degR]

    // No loss in Static Pressure or Static Temperature (Isentropic)
    Pt7 = Pt5;
    Tt7 = Tt5;

    // Pressure ratio at Nozzle
    pi_n = Pt7/Pt5;

    // Temp. ratio at Nozzle
    tao_n = Tt7/Tt5;

    // Mach number at Nozzle Exit
    M_7 = Math.sqrt((2/(gamma-1))*((tao_r*tao_c*tao_t)-1));

    // Velocity ratio at Nozzle exit
    U_7_over_a_7 = M_7;

    // Velocity ratio over Ambient speed of sound
    U_7_over_a_zero = Math.sqrt((2/(gamma-1))*(tao_lambda/(tao_r*tao_c))*((tao_r*tao_c*tao_t)-1));

    if (afterburnerOn) {
      // fuel flow (ratio)
      f_ab = ((Cp*Tzero)/hpr)*(tao_lambda_ab-tao_r);

      // Temperature ratio Nozzle to Afterburner
      Tt7_over_Tt5 = (tao_lambda_ab/(tao_t*tao_lambda));

      // ((U7_ab)/a0)^2
      U_7_ab_over_a_zero_squared = (Tt7_over_Tt5 * Math.pow(U_7_over_a_zero, 2));

      // U7_ab  Exhaust Velocity at Nozzle with AB ON
      U_7_ab = (Math.sqrt(U_7_ab_over_a_zero_squared))*azero;

      // U_0  Velocity at inlet (speed of aircraft)
      U_0 = azero*Mzero;

      // U_9 Velocity at Fan Nozzle Exhaust
      U_9 = (Math.sqrt(U_9_over_a_zero_squared))*azero;

      // Specific Thrust
      F_s_ab = ((azero/gc)*(1/(1+alpha)))*(Math.sqrt(U_7_ab_over_a_zero_squared)-Mzero+ (alpha*(Math.sqrt(U_9_over_a_zero_squared)-Mzero)));

      // Thrust [lbf]
      F_7_ab = F_s_ab*mdot_zero;

      // Thrust Specific Fuel Consumption (TSFC)
      TSFC_ab = (f_ab/F_s_ab)*3600;

      // Thermal Efficiency of Turbojet Engine
      eta_t_ab = (gamma-1)*Cp*Tzero*((U_7_ab_over_a_zero_squared - Math.pow(Mzero, 2))/(2*f_ab*hpr));

      // Performance Efficiency of Turbojet Engine
      eta_p_ab = 2*(((U_7_ab/U_0)-1+(alpha*((U_9/U_0)-1)))/(((Math.pow(U_7_ab, 2))/(Math.pow(U_0, 2)))-1+(alpha*(((Math.pow(U_9, 2)/Math.pow(U_0,2))-1)))));

      // Overall Turbojet Engine Efficiency
      eta_o_ab = eta_t_ab*eta_p_ab;
    } else {
      // Specific Thrust
      F_s = (azero/gc)*(U_7_over_a_zero-Mzero);

      // Thrust [lbf]
      F_7 = F_s*mdot_zero;

      // Thrust Specific Fuel Consumption (TSFC)
      TSFC = (f/F_s)*3600;

      // Thermal Efficiency of Turbojet Engine
      eta_t = 1-(1/(tao_r*tao_c));

      // Performance Efficiency of Turbojet Engine
      eta_p = (2*Mzero)/(U_7_over_a_zero+Mzero);

      // Overall Turbojet Engine Efficiency
      eta_o = eta_t*eta_p;
    }

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "P7", Value.theType.eDouble, P7, "[lbf/ft^2]", "", "", true);
      valueSet.addValue(name, "T7", Value.theType.eDouble, T7, "[degR]", "", "", true);
      valueSet.addValue(name, "Pt7", Value.theType.eDouble, Pt7, "[lbf/ft^2]", "", "", true);
      valueSet.addValue(name, "Tt7", Value.theType.eDouble, Tt7, "[degR]", "", "", true);
      valueSet.addValue(name, "pi_n", Value.theType.eDouble, pi_n, "", "Pressure ratio at Nozzle", "", true);
      valueSet.addValue(name, "tao_n", Value.theType.eDouble, tao_n, "", "Temp. ratio at Nozzle", "", true);
      valueSet.addValue(name, "M_7", Value.theType.eDouble, M_7, "", "Mach number at Nozzle Exit", "", true);
      valueSet.addValue(name, "U_7_over_a_7", Value.theType.eDouble, U_7_over_a_7, "", "Velocity ratio at Nozzle exit", "U7/a7", true);
      valueSet.addValue(name, "U_7_over_a_zero", Value.theType.eDouble, U_7_over_a_zero, "", "Velocity ratio over Ambient speed of sound", "U7/a0", true);
      if (afterburnerOn) {
        valueSet.addValue(name, "f_ab", Value.theType.eDouble, f_ab, "", "fuel flow (ratio)", "", true);
        valueSet.addValue(name, "Tt7_over_Tt5", Value.theType.eDouble, Tt7_over_Tt5, "", "Temperature ratio Nozzle to Afterburner", "Tt7/Tt5", true);
        valueSet.addValue(name, "U_7_ab_over_a_zero_squared", Value.theType.eDouble, U_7_ab_over_a_zero_squared, "", "", "((U7_ab)/a0)^2", true);
        valueSet.addValue(name, "U_7_ab", Value.theType.eDouble, U_7_ab, "", "Exhaust Velocity at Nozzle with AB ON", "", true);
        valueSet.addValue(name, "U_0", Value.theType.eDouble, U_0, "", "Velocity at inlet (speed of aircraft)", "", true);
        valueSet.addValue(name, "U_9", Value.theType.eDouble, U_9, "", "Velocity at Fan Nozzle Exhaust", "", true);
        valueSet.addValue(name, "F_s_ab", Value.theType.eDouble, F_s_ab, "[lbf/(lbm/s)]", "Specific Thrust", "", true);
        valueSet.addValue(name, "F_7_ab", Value.theType.eDouble, F_7_ab, "[lbf]", "(Total) Thrust", "", true);
        valueSet.addValue(name, "TSFC_ab", Value.theType.eDouble, TSFC_ab, "[(lbm/hr)/lbf] or [1/hr]", "Thrust Specific Fuel Consumption (TSFC)", "", true);
        valueSet.addValue(name, "eta_t_ab", Value.theType.eDouble, eta_t_ab, "", "Thermal Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_p_ab", Value.theType.eDouble, eta_p_ab, "", "Performance Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_o_ab", Value.theType.eDouble, eta_o_ab, "", "Overall Turbojet Engine Efficiency", "", true);
      } else {
        valueSet.addValue(name, "F_s", Value.theType.eDouble, F_s, "[lbf/(lbm/s)]", "Specific Thrust", "", true);
        valueSet.addValue(name, "F_7", Value.theType.eDouble, F_7, "[lbf]", "(Total) Thrust", "", true);
        valueSet.addValue(name, "TSFC", Value.theType.eDouble, TSFC, "[(lbm/hr)/lbf] or [1/hr]", "Thrust Specific Fuel Consumption (TSFC)", "", true);
        valueSet.addValue(name, "eta_t", Value.theType.eDouble, eta_t, "", "Thermal Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_p", Value.theType.eDouble, eta_p, "", "Performance Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_o", Value.theType.eDouble, eta_o, "", "Overall Turbojet Engine Efficiency", "", true);
      }
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

    // Expanded to Ambient Pressure

    // Pressure ratio at Nozzle Exit to Ambient
    Pzero_over_P7 = 0.9;

    // Pressure at Nozzle Exit
    P7 = Pzero/Pzero_over_P7;  //[lbf/ft^2]

    // Total Pressure at Nozzle Exit [lbf/ft^2]
    Pt7 = Pzero*pi_r*pi_d*pi_c*pi_b*pi_t*pi_n;

    // Total Temperature at Nozzle Exit [degR]
    T7 = Tzero* ((tao_lambda*tao_t)/(Math.pow((Pt7/P7),((gamma_t-1)/gamma_t)))*(Cp_c/Cp_t));

    // No loss in Static Pressure or Static Temperature (Isentropic)

    // Total Temperature at Nozzle Exit [degR]
    Tt7 = Tt6;

    // Temp. ratio at Nozzle
    tao_n = Tt7/Tt6;

    // Mach number at Nozzle Exit
    M_7 = Math.sqrt((2/(gamma_t-1))*((Math.pow((Pt7/P7),((gamma_t-1)/gamma_t)))-1));

    // Velocity ratio at Nozzle exit
    U_7_over_a_7 = M_7;

    // Velocity ratio over Ambient speed of sound
    U_7_over_a_zero = M_7*Math.sqrt((gamma_t/gamma_c)*(Rair_t/Rair_c)*(T7/Tzero));
    U_7_over_a_zero_squared = (U_7_over_a_zero*U_7_over_a_zero);

    if (afterburnerOn) {
      // fuel flow (ratio) with Afterburner ON
      f_ab = ((Cp_t*Tzero)/hpr)*(tao_lambda_ab-tao_r);

      // Temperature ratio Nozzle to Afterburner
      Tt7_over_Tt5 = (tao_lambda_ab/(tao_t*tao_lambda));

      // Velocity ratio at Nozzle exit to Ambient
      // ((U7_ab)/a0)^2
      U_7_ab_over_a_zero_squared = (Tt7_over_Tt5 * Math.pow(U_7_over_a_zero, 2));

      // (U7_ab)/a0
      U_7_ab_over_a_zero = Math.sqrt(U_7_ab_over_a_zero_squared);

      // U7_ab  Exhaust Velocity at Nozzle with AB ON [ft/s]
      U_7_ab = (Math.sqrt(U_7_ab_over_a_zero_squared))*azero;

      // U_0  Velocity at inlet (speed of aircraft) [ft/s]
      U_0 = azero*Mzero;

      // U_9 Velocity at Fan Nozzle Exhaust [ft/s]
      U_9 = (Math.sqrt(U_9_over_a_zero_squared))*azero;

      // Specific Thrust

      // Specific Thrust through core w/AB  [lbf/(lbm/s)]
      F_s_c_ab = (azero/gc)*((1+f_ab)*(U_7_ab_over_a_zero)-Mzero+(1+f_ab)*(Rair_t/Rair_c)*((T7/Tzero)/(U_7_ab_over_a_zero))*((1-(Pzero/P7))/gamma_c));

      // (Total) Specific Thrust w/AB  [lbf/(lbm/s)]
      F_s_ab = ( (1/(1+alpha))*F_s_c_ab  ) + ( (alpha/(1+alpha))*F_s_fan);

      // (Total) Thrust [lbf]
      F_ab = F_s_ab*mdot_zero;

      // Thrust Specific Fuel Consumption (TSFC)  [1/hr]
      TSFC_ab = (1/(1+alpha))*(f_ab/F_s_ab)*3600;

      // Thermal Efficiency of Turbojet Engine
      eta_t_ab = ((Math.pow((azero), 2)/gc)*( (1+f_ab) *  (Math.pow(U_7_ab_over_a_zero, 2)) + alpha*(U_9_over_a_zero_squared) - (1+alpha)*Math.pow(Mzero, 2)))/(2*f_ab*hpr*778.17);

      // Performance Efficiency of Turbojet Engine
      eta_p_ab = (2*Mzero)* ( ( (1+f_ab)*(U_7_ab_over_a_zero)+(alpha)*(U_9_over_a_zero)-(1+alpha)*Mzero  )/( (1+f_ab)*(U_7_ab_over_a_zero_squared)+(alpha)*(U_9_over_a_zero_squared)-(1+alpha)*(Mzero*Mzero)  ));

      // Overall Turbojet Engine Efficiency
      eta_o_ab = eta_t_ab*eta_p_ab;
    } else {
      // Specific Thrust through core  [lbf/(lbm/s)]
      F_s_c = (azero/gc)*((1+f)*(U_7_over_a_zero)-Mzero+(1+f)*(Rair_t/Rair_c)*((T7/Tzero)/(U_7_over_a_zero))*((1-(Pzero/P7))/gamma_c));

      // Specific Thrust through fan  [lbf/(lbm/s)]
      F_s_fan = (azero/gc)*( (U_9_over_a_zero)-Mzero+(((T9/Tzero)/(U_9_over_a_zero))*( (1-(Pzero/P9))/gamma_c ) ));

      // (Total) Specific Thrust  [lbf/(lbm/s)]
      F_s = ( (1/(1+alpha))*F_s_c  ) + ( (alpha/(1+alpha))*F_s_fan);

      // (Total) Thrust [lbf]
      F = mdot_zero*F_s;

      // Thrust Specific Fuel Consumption (TSFC) [1/hr]
      TSFC = (1/(1+alpha))*(f/F_s)*3600;

      // Thermal Efficiency of Turbojet Engine
      eta_t = ((Math.pow((azero), 2)/gc)*( (1+f) *  (Math.pow( U_7_over_a_zero,2  )) + alpha*(U_9_over_a_zero_squared) - (1+alpha)*Math.pow(Mzero, 2)))/(2*f*hpr*778.17);

      // Performance Efficiency of Turbojet Engine
      eta_p = (2*Mzero)* ( ( (1+f)*(U_7_over_a_zero)+(alpha)*(U_9_over_a_zero)-(1+alpha)*Mzero  )/( (1+f)*(U_7_over_a_zero_squared)+(alpha)*(U_9_over_a_zero_squared)-(1+alpha)*(Mzero*Mzero)  ));

      // Overall Turbojet Engine Efficiency
      eta_o = eta_t*eta_p;
    }

    if (experimentState.isFirstPass()) {
      valueSet.addValue(name, "Pzero_over_P7", Value.theType.eDouble, Pzero_over_P7, "[ft^2]", "Pressure ratio at Nozzle Exit to Ambient", "P0/P7", true);
      valueSet.addValue(name, "P7", Value.theType.eDouble, P7, "[lbf/ft^2]", "Pressure at Nozzle Exit", "", true);
      valueSet.addValue(name, "T7", Value.theType.eDouble, T7, "[degR]", "Total Temperature at Nozzle Exit", "", true);
      valueSet.addValue(name, "Pt7", Value.theType.eDouble, Pt7, "[lbf/ft^2]", "Total Pressure at Nozzle Exit", "", true);
      valueSet.addValue(name, "Tt7", Value.theType.eDouble, Tt7, "[degR]", "Total Temperature at Nozzle Exit", "", true);
      valueSet.addValue(name, "tao_n", Value.theType.eDouble, tao_n, "", "Temp. ratio at Nozzle", "", true);
      valueSet.addValue(name, "M_7", Value.theType.eDouble, M_7, "", "Mach number at Nozzle Exit", "", true);
      valueSet.addValue(name, "U_7_over_a_7", Value.theType.eDouble, U_7_over_a_7, "", "Velocity ratio at Nozzle exit", "U7/a7", true);
      valueSet.addValue(name, "U_7_over_a_zero", Value.theType.eDouble, U_7_over_a_zero, "", "Velocity ratio over Ambient speed of sound", "U7/a0", true);
      valueSet.addValue(name, "U_7_over_a_zero_squared", Value.theType.eDouble, U_7_over_a_zero_squared, "", "Velocity ratio at Nozzle exit to Ambient", "(U7/a0)^2", true);
      if (afterburnerOn) {
        valueSet.addValue(name, "f_ab", Value.theType.eDouble, f_ab, "", "fuel flow (ratio)", "", true);
        valueSet.addValue(name, "Tt7_over_Tt5", Value.theType.eDouble, Tt7_over_Tt5, "", "Temperature ratio Nozzle to Afterburner", "Tt7/Tt5", true);
        valueSet.addValue(name, "U_7_ab_over_a_zero_squared", Value.theType.eDouble, U_7_ab_over_a_zero_squared, "", "Velocity ratio at Nozzle exit to Ambient", "((U7_ab)/a0)^2", true);
        valueSet.addValue(name, "U_7_ab_over_a_zero", Value.theType.eDouble, U_7_ab_over_a_zero, "[ft/s]", "Exhaust Velocity at Nozzle with AB ON", "(U7_ab)/a0", true);
        valueSet.addValue(name, "U_7_ab", Value.theType.eDouble, U_7_ab, "", "Exhaust Velocity at Nozzle with AB ON", "", true);
        valueSet.addValue(name, "U_0", Value.theType.eDouble, U_0, "[ft/s]", "Velocity at inlet (speed of aircraft)", "", true);
        valueSet.addValue(name, "U_9", Value.theType.eDouble, U_9, "[ft/s]", "Velocity at Fan Nozzle Exhaust", "", true);
        valueSet.addValue(name, "F_s_c_ab", Value.theType.eDouble, F_s_c_ab, "[lbf/(lbm/s)]", "Specific Thrust through core w/AB", "", true);
        valueSet.addValue(name, "F_s_ab", Value.theType.eDouble, F_s_ab, "[lbf/(lbm/s)]", "(Total) Specific Thrust w/AB", "", true);
        valueSet.addValue(name, "F_ab", Value.theType.eDouble, F_ab, "[lbf]", "(Total) Thrust", "", true);
        valueSet.addValue(name, "TSFC_ab", Value.theType.eDouble, TSFC_ab, "[(lbm/hr)/lbf] or [1/hr]", "Thrust Specific Fuel Consumption (TSFC)", "", true);
        valueSet.addValue(name, "eta_t_ab", Value.theType.eDouble, eta_t_ab, "", "Thermal Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_p_ab", Value.theType.eDouble, eta_p_ab, "", "Performance Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_o_ab", Value.theType.eDouble, eta_o_ab, "", "Overall Turbojet Engine Efficiency", "", true);
      } else {
        valueSet.addValue(name, "F_s_c", Value.theType.eDouble, F_s_c, "[lbf/(lbm/s)]", "Specific Thrust through core", "", true);
        valueSet.addValue(name, "F_s_fan", Value.theType.eDouble, F_s_fan, "[lbf/(lbm/s)]", "Specific Thrust through fan", "", true);
        valueSet.addValue(name, "F_s", Value.theType.eDouble, F_s, "[lbf/(lbm/s)]", "Specific Thrust", "", true);
        valueSet.addValue(name, "F", Value.theType.eDouble, F, "[lbf]", "(Total) Thrust", "", true);
        valueSet.addValue(name, "TSFC", Value.theType.eDouble, TSFC, "[(lbm/hr)/lbf] or [1/hr]", "Thrust Specific Fuel Consumption (TSFC)", "", true);
        valueSet.addValue(name, "eta_t", Value.theType.eDouble, eta_t, "", "Thermal Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_p", Value.theType.eDouble, eta_p, "", "Performance Efficiency of Turbojet Engine", "", true);
        valueSet.addValue(name, "eta_o", Value.theType.eDouble, eta_o, "", "Overall Turbojet Engine Efficiency", "", true);
      }
    }
    valueSet.valuePush(this);
  }
}
