package net.sf.latexdraw.actions;

import javax.swing.JLabel;
import net.sf.latexdraw.instruments.EditionChoice;
import net.sf.latexdraw.instruments.Pencil;

import org.malai.action.Action;

/**
 * This action allows to set the kind of shape that the pencil must draw.<br>
 * <br>
 * This file is part of LaTeXDraw.<br>
 * Copyright (c) 2005-2014 Arnaud BLOUIN<br>
 * <br>
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * <br>
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br>
 * <br>
 * 05/14/2010<br>
 * @author Arnaud BLOUIN
 * @since 3.0
 */
public class ModifyPencilStyle extends Action {
	/** The pencil to set. */
	private Pencil pencil;

	/** The new editing choice to set. */
	private EditionChoice editingChoice;

	private JLabel statusBar;


    @Override
	public void flush() {
		super.flush();
		pencil 			= null;
		editingChoice 	= null;
		statusBar 		= null;
	}


	@Override
	protected void doActionBody() {
		pencil.setCurrentChoice(editingChoice);

		if(statusBar != null) {
			statusBar.setText(editingChoice.getExplanations());
			statusBar.setVisible(true);
		}
	}



	@Override
	public boolean canDo() {
		return pencil!=null && editingChoice!=null && pencil.currentChoice()!=editingChoice;
	}



	@Override
	public boolean isRegisterable() {
		return false;
	}


	/**
	 * Sets the pencil to parameterise.
	 * @param pencil The pencil.
	 * @since 3.0
	 */
	public void setPencil(final Pencil pencil) {
		this.pencil = pencil;
	}


	/**
	 * Sets the new editing choice of the pencil.
	 * @param editingChoice The new editing choice (can be null).
	 * @since 3.0
	 */
	public void setEditingChoice(final EditionChoice editingChoice) {
		this.editingChoice = editingChoice;
	}

	/**
	 * Sets the status bar.
	 * @param bar The JLabel used as the status bar. Can be null.
	 */
	public void setStatusBar(final JLabel bar) {
		statusBar = bar;
	}
}
