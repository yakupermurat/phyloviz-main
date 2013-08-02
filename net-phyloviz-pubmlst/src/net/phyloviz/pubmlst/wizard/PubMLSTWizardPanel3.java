package net.phyloviz.pubmlst.wizard;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.event.ChangeListener;
import net.phyloviz.core.data.AbstractProfile;
import net.phyloviz.core.data.DataSet;
import net.phyloviz.core.data.DataSetTracker;
import net.phyloviz.core.data.Population;
import net.phyloviz.core.data.TypingData;
import net.phyloviz.core.util.PopulationFactory;
import net.phyloviz.core.util.TypingFactory;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

public class PubMLSTWizardPanel3 implements WizardDescriptor.ValidatingPanel {

	/**
	 * The visual component that displays this panel. If you need to access the
	 * component from this class, just use getComponent().
	 */
	private Component component;
	private String dataSetName;
	private String sDBShort;
	private TypingData<? extends AbstractProfile> td;
	private TypingFactory tf;
	private Population pop;
	private DataSet ds;

	// Get the visual component for the panel. In this template, the component
	// is kept separate. This can be more efficient: if the wizard is created
	// but never displayed, or not all panels are displayed, it is better to
	// create only those which really need to be visible.
	@Override
	public Component getComponent() {
		if (component == null) {
			component = new PubMLSTVisualPanel3();
		}
		return component;
	}

	@Override
	public HelpCtx getHelp() {
		// Show no Help button for this panel:
		return HelpCtx.DEFAULT_HELP;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public final void addChangeListener(ChangeListener l) {
	}

	@Override
	public final void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void readSettings(Object settings) {
		((WizardDescriptor) settings).putProperty("WizardPanel_image", ImageUtilities.loadImage("net/phyloviz/core/PopulationImage.png", true));
		td = (TypingData<AbstractProfile>) ((WizardDescriptor) settings).getProperty("typing_data");
		tf = (TypingFactory) ((WizardDescriptor) settings).getProperty("typing_factory");
		dataSetName = (String) ((WizardDescriptor) settings).getProperty("name");
		sDBShort = (String) ((WizardDescriptor) settings).getProperty("dbNameShort");
		((PubMLSTVisualPanel3) getComponent()).setDatabase(sDBShort);
	}

	@Override
	public void storeSettings(Object settings) {
	}

	@Override
	public void validate() throws WizardValidationException {
		ds = new DataSet(dataSetName);

		if (((PubMLSTVisualPanel3) getComponent()).hasPubMLSTData()
				|| ((PubMLSTVisualPanel3) getComponent()).hasFileData()) {

			pop = null;
			if (((PubMLSTVisualPanel3) getComponent()).hasFileData()) {

				String fileName = ((PubMLSTVisualPanel3) getComponent()).getFilename();
				if (fileName != null && (!fileName.equals(""))) {
					try {
						StatusDisplayer.getDefault().setStatusText("Loading isolate data...");
						pop = new PopulationFactory().loadPopulation(new FileReader(fileName));
					} catch (FileNotFoundException ex) {
						throw new WizardValidationException(null, "File not found: " + ex.getMessage(), null);
					} catch (IOException ex) {
						throw new WizardValidationException(null, "Error loading file: " + ex.getMessage(), null);
					} catch (Exception ex) {
						throw new WizardValidationException(null, "Fatal error: " + ex.getMessage(), null);
					}
				} else {
					throw new WizardValidationException(null, "No file specified!", null);
				}

			} else {

				if (((PubMLSTVisualPanel3) getComponent()).isEmpty()) {
					throw new WizardValidationException(null, "No isolate data completely loaded yet!", null);
				}
				StatusDisplayer.getDefault().setStatusText("Loading isolate data...");
				StringReader sr = ((PubMLSTVisualPanel3) getComponent()).getIsolateData();
				try {
					pop = new PopulationFactory().loadPopulation(sr);
				} catch (IOException ex) {
					throw new WizardValidationException(null, "Error loading isolates: " + ex.getMessage(), null);
				}

			}

			if (pop != null) {

				int key = ((PubMLSTVisualPanel3) getComponent()).getKeyIndex();
				ds.add(pop);
				StatusDisplayer.getDefault().setStatusText("Integrating data...");
				td = tf.integrateData(td, pop, key);
				ds.setPopKey(key);

			}
		}

		ds.add(td);
		Lookup.getDefault().lookup(DataSetTracker.class).add(ds);
		StatusDisplayer.getDefault().setStatusText("Dataset loaded.");
	}
}
