package tr.org.liderahenk.script.dialogs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.IProfileDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.model.Profile;
import tr.org.liderahenk.script.constants.ScriptConstants;
import tr.org.liderahenk.script.i18n.Messages;
import tr.org.liderahenk.script.model.ScriptFile;
import tr.org.liderahenk.script.model.ScriptType;

public class ScriptProfileDialog implements IProfileDialog {

	private static final Logger logger = LoggerFactory.getLogger(ScriptProfileDialog.class);

	private ScriptFile selectedScript;
	
	private Combo cmbType;
	private Text txtScriptParams;
	private Text txtContents;
	
	@Override
	public void init() {
	}

	@Override
	public void createDialogArea(Composite parent, Profile profile) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Script type
		Label lblType = new Label(composite, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblType.setText(Messages.getString("SCRIPT_TYPE"));

		cmbType = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		ScriptType[] values = ScriptType.values();
		boolean selected = false;
		for (int i = 0; i < values.length; i++) {
			String i18n = Messages.getString(values[i].toString().toUpperCase(Locale.ENGLISH));
			cmbType.add(i18n);
			cmbType.setData(i18n, values[i]);
			if (getScriptType(profile)!= null && getScriptType(profile).toUpperCase().equals(values[i].toString())) {
				cmbType.select(i);
				selected = true;
			}
		}
		cmbType.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedType() == ScriptType.BASH) {
					txtContents.setText(ScriptConstants.TEMPLATES.BASH);
				} else if (getSelectedType() == ScriptType.PERL) {
					txtContents.setText(ScriptConstants.TEMPLATES.PERL);
				} else if (getSelectedType() == ScriptType.RUBY) {
					txtContents.setText(ScriptConstants.TEMPLATES.RUBY);
				} else if (getSelectedType() == ScriptType.PYTHON) {
					txtContents.setText(ScriptConstants.TEMPLATES.PYTHON);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		Label lblScriptParams = new Label(composite, SWT.NONE);
		lblScriptParams.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblScriptParams.setText(Messages.getString("SCRIPT_PARAMETERS"));

		txtScriptParams = new Text(composite, SWT.BORDER);
		txtScriptParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Contents
		Label lblContents = new Label(composite, SWT.NONE);
		lblContents.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblContents.setText(Messages.getString("CONTENTS"));
		
		txtContents = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 350;
		gridData.widthHint = 600;
		txtContents.setLayoutData(gridData);
		
		if (!selected) {
			cmbType.select(0);
			txtContents.setText(ScriptConstants.TEMPLATES.BASH);
		}
		
		String scriptContent = getScriptContent(profile);
		if (scriptContent != null) {
			txtContents.setText(scriptContent);
		}
		
		String scriptParameters = getScriptParameters(profile);
		if (scriptParameters != null) {
			txtScriptParams.setText(scriptParameters);
		}
	}

	protected void handleSelection() {
		
	}

	@Override
	public Map<String, Object> getProfileData() throws Exception {
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		// SCRIPT_PARAMS may contain script parameters or it can be empty string
		parameterMap.put("SCRIPT_PARAMS", txtScriptParams.getText());
		parameterMap.put("SCRIPT_CONTENTS", txtContents.getText());
		parameterMap.put("SCRIPT_TYPE", cmbType.getItem(cmbType.getSelectionIndex()).toUpperCase());
		return parameterMap;
	}

	protected String getScriptContent(Profile profile) {
		return profile != null && profile.getProfileData() != null
				&& profile.getProfileData().get(ScriptConstants.PARAMETERS.SCRIPT) != null
						? profile.getProfileData().get(ScriptConstants.PARAMETERS.SCRIPT).toString() : null;
	}
	
	protected String getScriptParameters(Profile profile) {
		return profile != null && profile.getProfileData() != null
				&& profile.getProfileData().get(ScriptConstants.PARAMETERS.PARAMETERS) != null
						? profile.getProfileData().get(ScriptConstants.PARAMETERS.PARAMETERS).toString() : null;
	}
	
	protected String getScriptType(Profile profile) {
		return profile != null && profile.getProfileData() != null
				&& profile.getProfileData().get(ScriptConstants.PARAMETERS.SCRIPT_TYPE) != null
						? profile.getProfileData().get(ScriptConstants.PARAMETERS.SCRIPT_TYPE).toString() : null;
	}
	
	@Override
	public void validateBeforeSave() throws ValidationException {
		if (txtContents.getText().isEmpty()) {
			throw new ValidationException(Messages.getString("FILL_CONTENTS_FIELD"));
		}
	}

	private ScriptType getSelectedType() {
		int selectionIndex = cmbType.getSelectionIndex();
		if (selectionIndex > -1 && cmbType.getItem(selectionIndex) != null
				&& cmbType.getData(cmbType.getItem(selectionIndex)) != null) {
			return (ScriptType) cmbType.getData(cmbType.getItem(selectionIndex));
		}
		return null;
	}
}
