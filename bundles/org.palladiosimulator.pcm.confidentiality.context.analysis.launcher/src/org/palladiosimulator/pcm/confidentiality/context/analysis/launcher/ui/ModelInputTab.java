package org.palladiosimulator.pcm.confidentiality.context.analysis.launcher.ui;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.confidentiality.context.analysis.launcher.Activator;
import org.palladiosimulator.pcm.confidentiality.context.analysis.launcher.constants.Constants;

import de.uka.ipd.sdq.workflow.launchconfig.tabs.TabHelper;

//TODO use Resourcebundles for Strings. ResourceBundle.getBundle
/**
 * Main configuration tab for the launch configuration
 * 
 * @author Mirko Sowa
 *
 */
public class ModelInputTab extends AbstractLaunchConfigurationTab {

    private final InitTaskExecutor initTaskExecutor = new InitTaskExecutor();

    private Composite comp;

    private Text repositoryTextField;
    private Text usageTextField;
    private Text allocationTextField;
    private Text contextTextField;
    private Text dataTextField;
    private Text adversaryTextField;
    private Text modificationTextField;
    private Combo analysisCombo;

    public ModelInputTab() {

        Activator sharedInstance = Activator.getInstance();
        if (sharedInstance != null) {
//			proversMap = sharedInstance.getProverManagerInstance().getProvers();
//			queryMap = sharedInstance.getQueryManagerInstance().getQueries();
        }
    }

    @Override
    public String getName() {
        return Constants.NAME.getConstant();
    }

    @Override
    public String getMessage() {
        return "Please select specified files.";
    }

    @Override
    public boolean isValid(final ILaunchConfiguration launchConfig) {
//        return !repositoryTextField.getText().isEmpty() && !allocationTextField.getText().isEmpty()
//                && !contextTextField.getText().isEmpty() && isURIexistent(repositoryTextField.getText())
//                && isURIexistent(allocationTextField.getText()) && isURIexistent(contextTextField.getText());
        // FIXME
        return true;
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        initTaskExecutor.runInitTask(() -> {
            configuration.setAttribute(Constants.REPOSITORY_MODEL_LABEL.getConstant(), "");
            configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), "");
            configuration.setAttribute(Constants.CONTEXT_MODEL_LABEL.getConstant(), "");
            // ProverInformation
        });
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        initTaskExecutor.runInitTask(() -> {
            repositoryTextField.setText("");
            allocationTextField.setText("");
            contextTextField.setText("");

            try {
                repositoryTextField
                        .setText(configuration.getAttribute(Constants.REPOSITORY_MODEL_LABEL.getConstant(), ""));
                allocationTextField
                        .setText(configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
                contextTextField.setText(configuration.getAttribute(Constants.CONTEXT_MODEL_LABEL.getConstant(), ""));
                dataTextField.setText(configuration.getAttribute(Constants.DATA_MODEL_LABEL.getConstant(), ""));
                adversaryTextField
                        .setText(configuration.getAttribute(Constants.ADVERSARY_MODEL_LABEL.getConstant(), ""));
                usageTextField.setText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
                analysisCombo.setText(configuration.getAttribute(Constants.ANALYSIS_TYPE_LABEL.getConstant(), ""));
                modificationTextField
                        .setText(configuration.getAttribute(Constants.MODIFIACTION_MODEL_LABEL.getConstant(), ""));

            } catch (CoreException e) {
                // TODO expection handling
            }
        });
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(Constants.REPOSITORY_MODEL_LABEL.getConstant(), repositoryTextField.getText());
        configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), allocationTextField.getText());
        configuration.setAttribute(Constants.CONTEXT_MODEL_LABEL.getConstant(), contextTextField.getText());
        configuration.setAttribute(Constants.DATA_MODEL_LABEL.getConstant(), dataTextField.getText());
        configuration.setAttribute(Constants.ADVERSARY_MODEL_LABEL.getConstant(), adversaryTextField.getText());
        configuration.setAttribute(Constants.ANALYSIS_TYPE_LABEL.getConstant(), analysisCombo.getText());
        configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), usageTextField.getText());
        configuration.setAttribute(Constants.MODIFIACTION_MODEL_LABEL.getConstant(), modificationTextField.getText());
    }

    @Override
    public void createControl(Composite parent) {

        /* Modify listener for text input changes, sets dirty */
        final ModifyListener modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!initTaskExecutor.isInitTaskRunning()) {
                    setDirty(true);
                    updateLaunchConfigurationDialog();
                }
            }

        };
        final SelectionListener selectionListener = new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
                updateLaunchConfigurationDialog();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }

        };

        comp = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        comp.setLayout(layout);
        setControl(comp);

        var analysisGroup = new Group(comp, SWT.NONE);
        analysisGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        analysisGroup.setText(Constants.ANALYSIS_TYPE_LABEL.getConstant());
        analysisGroup.setLayout(layout);

        analysisCombo = new Combo(analysisGroup, SWT.DROP_DOWN);

        String[] items = new String[] { "Scenario", "Insider", "Attack surface" };

        analysisCombo.setItems(items);

        analysisCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        analysisCombo.addSelectionListener(selectionListener);
        analysisCombo.clearSelection();

        /* Usage Model */

        usageTextField = new Text(comp, SWT.BORDER);
        usageTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.USAGE_MODEL_LABEL.getConstant(),
                new String[] { "*.usagemodel" }, usageTextField, Display.getCurrent().getActiveShell(), "");

        /* Repository */

        repositoryTextField = new Text(comp, SWT.BORDER);
        repositoryTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.REPOSITORY_MODEL_LABEL.getConstant(),
                new String[] { "*.repository" }, repositoryTextField, Display.getCurrent().getActiveShell(), "");

        /* Allocation-Model */

        allocationTextField = new Text(comp, SWT.BORDER);
        allocationTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.ALLOCATION_MODEL_LABEL.getConstant(),
                new String[] { "*.allocation" }, allocationTextField, Display.getCurrent().getActiveShell(), "");

        /* Context-Model */

        contextTextField = new Text(comp, SWT.BORDER);
        contextTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.CONTEXT_MODEL_LABEL.getConstant(),
                new String[] { "*.context" }, contextTextField, Display.getCurrent().getActiveShell(), "");

        dataTextField = new Text(comp, SWT.BORDER);
        dataTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.DATA_MODEL_LABEL.getConstant(),
                new String[] { "*.confidentiality" }, dataTextField, Display.getCurrent().getActiveShell(), "");

        adversaryTextField = new Text(comp, SWT.BORDER);
        adversaryTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.ADVERSARY_MODEL_LABEL.getConstant(),
                new String[] { "*.attacker" }, adversaryTextField, Display.getCurrent().getActiveShell(), "");

        modificationTextField = new Text(comp, SWT.BORDER);
        modificationTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        TabHelper.createFileInputSection(comp, modifyListener, Constants.MODIFIACTION_MODEL_LABEL.getConstant(),
                new String[] { "*.kamp4attackmodificationmarks" }, modificationTextField, Display.getCurrent().getActiveShell(), "");
    }

    /**
     * Checks if a given URI is existent in the file system
     * 
     * @param s
     *            String of the URI
     * @return true if URI exists, else false.
     */
    private boolean isURIexistent(String s) {
        URIConverter uriConverter = new ResourceSetImpl().getURIConverter();
        URI uriFromText = URI.createURI(repositoryTextField.getText());
        if (uriConverter.exists(uriFromText, null)) {
            return true;
        }
        uriFromText = null;
        File usageFile = new File(s);
        if (usageFile != null && usageFile.exists()) {
            uriFromText = URI.createFileURI(usageFile.getAbsolutePath());
            if (uriConverter.exists(uriFromText, null)) {
                return true;
            }
        }
        return false;
    }

}
