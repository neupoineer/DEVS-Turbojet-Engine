/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-09-16
 */
package TurbojetEngineMod;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import experiment.toolkit.Calibration;
import model.modeling.IODevs;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

/**
 * The Class TurbojetEngine.
 */
public class TurbojetEngine extends ViewableDigraph {

  /** The version. */
  private char version;

  /** The calibration. */
  private final Calibration calibration = new Calibration(false);

  /** The models. */
  private final Vector<ViewableAtomic> models = new Vector<ViewableAtomic>();

  /** The Ke TJE T bool black box. */
  private final boolean KeTJET_Bool_BlackBox;

  /** The number of simview rows. */
  private final int KeTJET_Cnt_SimViewRows;

  /** The fluid model. */
  private final String KeTJET_Str_FluidModel;

  /**
   * Instantiates a new turbojet engine.
   */
  public TurbojetEngine() {
    this("Turbojet Engine", 'A');
  }

  /**
   * Instantiates a new turbojet engine.
   *
   * @param name the name
   * @param version the version
   */
  public TurbojetEngine(final String name, final char version) {
    super(Character.valueOf(version) + ": " + name);
    this.version = version;
    KeTJET_Bool_BlackBox = false;
    KeTJET_Cnt_SimViewRows = 1;
    KeTJET_Str_FluidModel = "undefined";

    final String calibrationFilename = FilenameUtils.getBaseName(calibration.getFilename()) + version + '.' + FilenameUtils.getExtension(calibration.getFilename());
    calibration.load(calibrationFilename);
    calibration.update(this);

    setBlackBox(KeTJET_Bool_BlackBox);

    if (!KeTJET_Str_FluidModel.equals("undefined"))
      this.name = Character.valueOf(version) + ": " + KeTJET_Str_FluidModel;

    defineLayout();
  }

  /**
   * Define layout.
   */
  private void defineLayout() {
    createEngineStage("Mission Setup");
    createEngineStage("Diffuser");
    createEngineStage("(Bypass) Fan");
    createEngineStage("(Bypass) Fan/Nozzle");
    createEngineStage("Compressor");
    createEngineStage("Combustor");
    createEngineStage("Turbine");
    createEngineStage("Afterburner");
    createEngineStage("Nozzle");

    addInport("xIn");
    addOutport("xOut");
    baseCoupling(this, models.get(0));
    for (int i = 0; i < models.size() - 1; i++)
      baseCoupling(models.get(i), models.get(i+1));
    baseCoupling(models.get(models.size() - 1), this);
  }

  /**
   * Creates the engine stage.
   *
   * @param name the name
   */
  private void createEngineStage(final String name) {
    final ViewableAtomic model = new EngineModelBase(name, version, calibration);
    if (((EngineModelBase)model).manifestModel()) {
      models.add(model);
      add(model);
    }
  }

  /**
   * Base coupling.
   *
   * @param prev the prev
   * @param next the next
   */
  private void baseCoupling(final IODevs prev, final IODevs next) {
    final String inPort = prev == this ? "xIn" : "xOut";
    final String outPort = next == this ? "xOut" : "xIn";
    addCoupling(prev, inPort, next, outPort);
    if (prev != this && next != this)
      addCoupling(next, "yOut", prev, "yIn");
  }

  /**
   * Gets the model count.
   *
   * @return the model count
   */
  public int getModelCount() {
    return models.size();
  }

  /**
   * Gets the height.
   *
   * @return the height
   */
  public int getHeight() {
    return 80 * KeTJET_Cnt_SimViewRows;
  }

  /**
   * Gets the width.
   *
   * @return the width
   */
  public int getWidth() {
    int width = 190 * (int)(models.size() / KeTJET_Cnt_SimViewRows);
    if ((models.size() % KeTJET_Cnt_SimViewRows) != 0)
      width += 190;
    width += 90;
    return width;
  }

  /* (non-Javadoc)
   * @see view.modeling.ViewableDigraph#layoutForSimViewOverride()
   */
  @Override
  public boolean layoutForSimViewOverride()
  {
    int pointX = 0;
    int pointY = 20;
    for (int i = 0; i < models.size(); i++) {
      int interval = i % KeTJET_Cnt_SimViewRows;
      models.get(i).setPreferredLocation(new Point(pointX, pointY + (80 * interval)));
      if (interval == (KeTJET_Cnt_SimViewRows - 1))
        pointX += 190;
    }
    preferredSize = new Dimension(getWidth(), getHeight());
    return true;
  }
}
