/*
 * This file is part of LaTeXDraw.
 * Copyright (c) 2005-2018 Arnaud BLOUIN
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package net.sf.latexdraw.commands;

import java.io.File;
import java.util.concurrent.ExecutionException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.latexdraw.badaboom.BadaboomCollector;
import net.sf.latexdraw.util.LangService;
import org.malai.command.CommandsRegistry;
import org.malai.javafx.command.IOCommand;
import org.malai.javafx.ui.JfxUI;
import org.malai.javafx.ui.OpenSaver;
import org.malai.undo.UndoCollector;

/**
 * This command permits to create a new drawing and initialises the application as required.
 * @author Arnaud Blouin
 */
public class NewDrawing extends IOCommand<Label> implements Modifying {
	/** The file chooser that will be used to select the location to save. */
	private FileChooser fileChooser;
	/** The instrument used that manage the preferences. */
	private final File currentFolder;
	private final LangService lang;
	private final Stage mainstage;

	public NewDrawing(final File file, final OpenSaver<Label> openSaveManager, final ProgressBar progressBar, final Label statusWidget, final JfxUI ui,
					final FileChooser fileChooser, final File currentFolder, final LangService lang, final Stage mainstage) {
		super(file, openSaveManager, progressBar, statusWidget, ui);
		this.fileChooser = fileChooser;
		this.currentFolder = currentFolder;
		this.lang = lang;
		this.mainstage = mainstage;
	}

	@Override
	protected void doCmdBody() {
		if(ui.isModified()) {
			final ButtonType type = SaveDrawing.showAskModificationsDialog(lang);

			if(type == ButtonType.NO) {
				newDrawing();
			}else {
				if(type == ButtonType.YES) {
					SaveDrawing.showDialog(fileChooser, true, file, currentFolder, ui, mainstage).ifPresent(f -> {
						try {
							openSaveManager.save(f.getPath(), progressBar, statusWidget).get();
						}catch(final InterruptedException | ExecutionException ex) {
							BadaboomCollector.INSTANCE.add(ex);
						}
						ui.setModified(false);
						newDrawing();
						ok = true;
					});
				}
			}
		}else {
			newDrawing();
			ok = true;
		}
	}


	protected void newDrawing() {
		ui.reinit();
		UndoCollector.INSTANCE.clear();
		CommandsRegistry.INSTANCE.clear();
	}


	@Override
	public boolean canDo() {
		return fileChooser != null && ui != null && openSaveManager != null;
	}


	@Override
	public void flush() {
		super.flush();
		fileChooser = null;
	}
}
