/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageLayer;
import com.asofterspace.toolbox.images.ImageLayerBasedOnImage;
import com.asofterspace.toolbox.images.ImageLayerBasedOnText;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuLayers {

	JMenu createMenu(GUI gui) {

		JMenu layers = new JMenu("Layers");

		JMenuItem delLayer = new JMenuItem("Delete Current Layer");
		delLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getPicture().deleteLayer(gui.getCurrentLayerIndex());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(delLayer);

		JMenuItem addImgLayer = new JMenuItem("Add Image Layer");
		addImgLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0,
					new Image(gui.getPicture().getWidth(), gui.getPicture().getHeight(), gui.getBackgroundColor()));
				gui.getPicture().addLayer(layer);
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(addImgLayer);

		JMenuItem addImgLayerFile = new JMenuItem("Add Image Layer (Opening Existing File)");
		addImgLayerFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				boolean returnImage = true;
				Image img = gui.openFile(returnImage);
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0, img);
				gui.getPicture().addLayer(layer);
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(addImgLayerFile);

		JMenuItem addImgLayerClipbrd = new JMenuItem("Add Image Layer (Pasting from Clipboard)");
		addImgLayerClipbrd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0, Image.createFromClipboard());
				gui.getPicture().addLayer(layer);
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(addImgLayerClipbrd);

		JMenuItem addImgLayerClickedArea = new JMenuItem("Add Image Layer (Based on Area Bounded by Last Two Clicks)");
		addImgLayerClickedArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int prevClickX = gui.getPrevClickX();
				int prevClickY = gui.getPrevClickY();
				int lastClickX = gui.getLastClickX();
				int lastClickY = gui.getLastClickY();
				int top = Math.min(prevClickY, lastClickY);
				int right = Math.max(prevClickX, lastClickX);
				int bottom = Math.max(prevClickY, lastClickY);
				int left = Math.min(prevClickX, lastClickX);

				gui.saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(left, top, gui.getPicture().bake().copy(top, right, bottom, left));
				gui.getPicture().addLayer(layer);
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(addImgLayerClickedArea);

		JMenuItem addTextLayer = new JMenuItem("Add Text Layer");
		addTextLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageLayerBasedOnText layer = new ImageLayerBasedOnText(0, 0, "Hello!", "Calibri", 32, gui.getForegroundColor());
				gui.getPicture().addLayer(layer);
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(addTextLayer);

		JMenuItem duplicateLayer = new JMenuItem("Duplicate Current Layer");
		duplicateLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getPicture().addLayer(gui.getCurrentLayer().copy());
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				gui.refreshLayerView();
			}
		});
		layers.add(duplicateLayer);

		layers.addSeparator();

		JMenuItem selLayerUp = new JMenuItem("Select One Layer Up");
		selLayerUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setCurrentLayerIndex(gui.getCurrentLayerIndex() + 1);
				gui.refreshLayerView();
			}
		});
		layers.add(selLayerUp);

		JMenuItem selLayerDown = new JMenuItem("Select One Layer Down");
		selLayerDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setCurrentLayerIndex(gui.getCurrentLayerIndex() - 1);
				gui.refreshLayerView();
			}
		});
		layers.add(selLayerDown);

		layers.addSeparator();

		JMenuItem moveLayerAllUp = new JMenuItem("Move Selected Layer All Layers Up / Front");
		moveLayerAllUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getPicture().moveLayerFullyUp(gui.getCurrentLayerIndex());
				gui.setCurrentLayerIndex(gui.getPicture().getLayerAmount() - 1);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveLayerAllUp);

		JMenuItem moveLayerUp = new JMenuItem("Move Selected Layer One Layer Up");
		moveLayerUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getPicture().moveLayerOneUp(gui.getCurrentLayerIndex());
				gui.setCurrentLayerIndex(gui.getCurrentLayerIndex() + 1);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveLayerUp);

		JMenuItem moveLayerDown = new JMenuItem("Move Selected Layer One Layer Down");
		moveLayerDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getPicture().moveLayerOneDown(gui.getCurrentLayerIndex());
				gui.setCurrentLayerIndex(gui.getCurrentLayerIndex() - 1);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveLayerDown);

		JMenuItem moveLayerAllDown = new JMenuItem("Move Selected Layer All Layers Down / Back");
		moveLayerAllDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getPicture().moveLayerFullyDown(gui.getCurrentLayerIndex());
				gui.setCurrentLayerIndex(0);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveLayerAllDown);

		layers.addSeparator();

		JMenuItem moveToLeft = new JMenuItem("Move Selected Layer to Left");
		moveToLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(0, curLayer.getOffsetY());
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToLeft);

		JMenuItem moveToRight = new JMenuItem("Move Selected Layer to Right");
		moveToRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(gui.getPicture().getWidth() - curLayer.getWidth(), curLayer.getOffsetY());
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToRight);

		JMenuItem moveToTop = new JMenuItem("Move Selected Layer to Top");
		moveToTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(curLayer.getOffsetX(), 0);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToTop);

		JMenuItem moveToBottom = new JMenuItem("Move Selected Layer to Bottom");
		moveToBottom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(curLayer.getOffsetX(), gui.getPicture().getHeight() - curLayer.getHeight());
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToBottom);

		JMenuItem moveToCenterH = new JMenuItem("Move Selected Layer to Center (Horizontally)");
		moveToCenterH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(
					(gui.getPicture().getWidth() - curLayer.getWidth()) / 2,
					curLayer.getOffsetY()
				);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToCenterH);

		JMenuItem moveToCenterV = new JMenuItem("Move Selected Layer to Center (Vertically)");
		moveToCenterV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(
					curLayer.getOffsetX(),
					(gui.getPicture().getHeight() - curLayer.getHeight()) / 2
				);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToCenterV);

		JMenuItem moveToCenter = new JMenuItem("Move Selected Layer to Center/Center");
		moveToCenter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				curLayer.moveTo(
					(gui.getPicture().getWidth() - curLayer.getWidth()) / 2,
					(gui.getPicture().getHeight() - curLayer.getHeight()) / 2
				);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		layers.add(moveToCenter);

		layers.addSeparator();

		JMenuItem convertCurToImg = new JMenuItem("Convert Current Layer to Image Layer");
		convertCurToImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = gui.getCurrentLayer();
				if (curLayer instanceof ImageLayerBasedOnText) {
					gui.getPicture().replaceLayer(gui.getCurrentLayerIndex(), curLayer.convertToImageLayerBasedOnImage());
					gui.refreshMainView();
					gui.refreshLayerView();
				}
			}
		});
		layers.add(convertCurToImg);

		JMenuItem bakeAllLayers = new JMenuItem("Bake All Layers Into One");
		bakeAllLayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setPicture(gui.getPicture().bake());
			}
		});
		layers.add(bakeAllLayers);

		return layers;
	}
}
