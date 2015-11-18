
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.phyloviz.project.action;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import net.phyloviz.algo.AbstractDistance;
import net.phyloviz.algo.DistanceProvider;
import net.phyloviz.core.data.AbstractProfile;
import net.phyloviz.core.data.DataSet;
import net.phyloviz.core.data.DataSetTracker;
import net.phyloviz.core.data.Population;
import net.phyloviz.core.data.TypingData;
import net.phyloviz.core.util.PopulationFactory;
import net.phyloviz.core.util.TypingFactory;
import net.phyloviz.project.ProjectItem;
import net.phyloviz.project.ProjectItemFactory;
import net.phyloviz.project.ProjectTypingDataFactory;
import net.phyloviz.upgmanjcore.visualization.PersistentVisualization;
import org.netbeans.api.project.Project;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

public final class LoadDataSetAction extends AbstractAction {

    private static final String VIZ_FOLDER = "visualization";

    public LoadDataSetAction() {
        putValue(Action.NAME, "Load DataSet");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        Properties prop = new Properties();
        String propFileName = "config.properties.pviz";

        String projectDir = getProjectLocation();
        File visualization = new File(projectDir, VIZ_FOLDER);

        try {

            InputStream inputStream = new FileInputStream(new File(projectDir, propFileName));
            prop.load(inputStream);

            StatusDisplayer.getDefault().setStatusText("Loading DataSet...");

            String dataSetName = prop.getProperty("dataset-name");
            if (dataSetAlreadyOpened(dataSetName)) {
                JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), "DataSet already opened!");
                WindowManager.getDefault().findTopComponent("DataSetExplorerTopComponent").requestActive();
                return;
            }

            String typingFactory = prop.getProperty("typing-factory"),
                    typingFile = prop.getProperty("typing-file"),
                    populationFactory = prop.getProperty("population-factory"),
                    populationFile = prop.getProperty("population-file"),
                    populationFK = prop.getProperty("population-foreign-key"),
                    algorithmOutput = prop.getProperty("algorithm-output"),
                    algorithmOutputFactory = prop.getProperty("algorithm-output-factory"),
                    algorithmOutputDistanceProvider = prop.getProperty("algorithm-output-distance"),
                    algorithmOutputLevel = prop.getProperty("algorithm-output-level"),
                    visualizations = prop.getProperty("visualization");

            String[] algoOutput = algorithmOutput != null
                    ? algorithmOutput.split(",")
                    : new String[]{""};
            String[] algoOutputFactory = algorithmOutputFactory != null
                    ? algorithmOutputFactory.split(",")
                    : new String[]{""};
            String[] algoOutputDistance = algorithmOutputDistanceProvider != null
                    ? algorithmOutputDistanceProvider.split(",")
                    : new String[]{""};
            String[] algoOutputLevel = algorithmOutputLevel != null
                    ? algorithmOutputLevel.split(",")
                    : new String[]{""};
            String[] viz = visualizations != null
                    ? visualizations.split(",")
                    : new String[]{};

            if (dataSetName != null && (!dataSetName.equals(""))) {

                DataSet ds = new DataSet(dataSetName);

                StatusDisplayer.getDefault().setStatusText("Loading typing data...");

                TypingFactory tf = null;
                TypingData<? extends AbstractProfile> td = null;

                Collection<? extends ProjectTypingDataFactory> tfLookup = (Lookup.getDefault().lookupAll(ProjectTypingDataFactory.class));
                for (ProjectTypingDataFactory ptdi : tfLookup) {
                    if (ptdi.getClass().getName().equals(typingFactory)) {
                        tf = (TypingFactory) ptdi;
                        td = ptdi.onLoad(new FileReader(new File(projectDir, typingFile)));
                        td.setDescription(tf.toString());
                        ds.add(ptdi);
                    }
                }

                if (populationFile != null && (!populationFile.equals("")) && populationFK != null) {

                    StatusDisplayer.getDefault().setStatusText("Loading isolate data...");
                    Population pop = ((PopulationFactory) Class.forName(populationFactory).newInstance())
                            .loadPopulation(new FileReader(new File(projectDir, populationFile)));
                    ds.add(pop);

                    int key = Integer.parseInt(populationFK);
                    StatusDisplayer.getDefault().setStatusText("Integrating data...");
                    td = tf.integrateData(td, pop, key);
                    ds.setPopKey(key);
                }

                int v_i = 0;
                Collection<? extends ProjectItemFactory> pifactory = (Lookup.getDefault().lookupAll(ProjectItemFactory.class));
                for (int i = 0; i < algoOutput.length; i++) {

                    for (ProjectItemFactory pif : pifactory) {
                        String pifName = pif.getClass().getName();
                        if (pifName.equals(algoOutputFactory[i])) {

                            PersistentVisualization pv = null;
                            if (viz.length > v_i && viz[v_i].split("\\.")[0].equals(algoOutput[i].split("\\.")[2])) {
                                try (FileInputStream fileIn = new FileInputStream(new File(visualization, viz[v_i++]))) {

                                    try (ObjectInputStream in = new ObjectInputStream(fileIn)) {

                                        pv = (PersistentVisualization) in.readObject();

                                    }

                                } catch (IOException | ClassNotFoundException e) {
                                    Exceptions.printStackTrace(e);
                                }
                            }
                            StatusDisplayer.getDefault().setStatusText("Loading algorithms...");
                            AbstractDistance ad = null;
                            Collection<? extends DistanceProvider> dpLookup = (Lookup.getDefault().lookupAll(DistanceProvider.class));
                            for (DistanceProvider dp : dpLookup) {
                                if (dp.getClass().getCanonicalName().equals(algoOutputDistance[i])) {
                                    ad = dp.getDistance(td);
                                }
                            }

                            ProjectItem pi = pif.loadData(ds, td, projectDir, algoOutput[i], ad, Integer.parseInt(algoOutputLevel[i]));
                            if (pi != null) {
                                if (pv != null) {
                                    pi.addPersistentVisualization(pv);
                                }
                                td.add(pi);
                            } else {
                                return;
                            }
                        }
                    }
                }

                ds.add(td);

                Lookup.getDefault().lookup(DataSetTracker.class).add(ds);
                StatusDisplayer.getDefault().setStatusText("Dataset loaded.");
                WindowManager.getDefault().findTopComponent("DataSetExplorerTopComponent").requestActive();

            } else {
                Exceptions.printStackTrace(new IllegalArgumentException("DataSet name not found!"));
            }
        } catch (IOException | HeadlessException | ClassNotFoundException | InstantiationException | IllegalAccessException | NumberFormatException e) {
            Exceptions.printStackTrace(e);
        }
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    private String getProjectLocation() {

        Lookup lookup = Utilities.actionsGlobalContext();
        Project project = lookup.lookup(Project.class
        );
        FileObject projectDir = project.getProjectDirectory();

        return projectDir.getPath();
    }

    private boolean dataSetAlreadyOpened(String dataSetName) {

        Lookup lk = Lookup.getDefault().lookup(DataSetTracker.class
        ).getLookup();

        Collection<? extends DataSet> data = lk.lookupAll(DataSet.class);
        for (DataSet ds : data) {
            if (ds.toString().equals(dataSetName)) {
                return true;
            }
        }

        return false;
    }
}