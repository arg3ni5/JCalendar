/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toedter.calendar;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Fabián
 */
public class RoundedFieldUI extends BasicTextFieldUI {

    private int round = 5;
    private int width, height;
    private int shadeWidth = 2;
    private int textSpacing = 3;
    private Color interior, bordeI, bordeE;
    private JTextComponent component;

    public RoundedFieldUI() {
        bordeE = Color.BLACK;
        interior = Color.WHITE;
        bordeI = Color.LIGHT_GRAY;
    }

    public RoundedFieldUI(RoundedFieldUI fieldUI) {
        bordeE = fieldUI.bordeE;
        interior = fieldUI.bordeE;
        bordeI = fieldUI.bordeE;
    }

    public RoundedFieldUI(RoundedFieldUI fieldUI, JTextComponent component) {
        this.component = component;
        bordeE = fieldUI.bordeE;
        interior = fieldUI.interior;
        bordeI = fieldUI.bordeI;
    }

    public RoundedFieldUI(Color interior, Color bordeI, Color bordeE) {
        this.interior = interior;
        this.bordeI = bordeI;
        this.bordeE = bordeE;
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        int s = shadeWidth + 1 + textSpacing;
        c.setBorder(BorderFactory.createEmptyBorder(s, s, s, s));
    }

    protected void paintSafely(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Shape border = getBorderShape();

        Stroke os = g2d.getStroke();
        g2d.setStroke(new BasicStroke(shadeWidth * 2));
        g2d.setPaint(bordeI);
        g2d.draw(border);
        g2d.setStroke(os);

        g2d.setPaint(interior);
        g2d.fill(border);

        g2d.setPaint(bordeE);
        g2d.draw(border);

        super.paintSafely(g);
    }

    public void setUI(JPanel panel, RoundedFieldUI uI) {

        for (Component c1 : panel.getComponents()) {
            if (c1.getClass() == JTextField.class) {
                ((JTextField) c1).setUI(new RoundedFieldUI(uI, (JTextComponent) c1));
            }
            if (c1.getClass() == JFormattedTextField.class) {
                ((JTextField) c1).setUI(new RoundedFieldUI(uI, (JTextComponent) c1));
            }
        }
    }

    private Shape getBorderShape() {
        component = getComponent();
        if (round > 0) {
            return new RoundRectangle2D.Double(
                    shadeWidth, shadeWidth,
                    component.getWidth() - shadeWidth * 2 - 1,
                    component.getHeight() - shadeWidth * 2 - 1,
                    round * 2, round * 2);
        } else {
            return new Rectangle2D.Double(
                    shadeWidth, shadeWidth,
                    component.getWidth() - shadeWidth * 2 - 1,
                    component.getHeight() - shadeWidth * 2 - 1);
        }
    }

    public void setComponent(JTextComponent component) {
        this.component = component;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);

        JPanel panel = new JPanel();
        panel.setSize(600, 600);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        frame.add(panel);

        panel.add(new JLabel("Field:"), BorderLayout.NORTH);

        JTextField field = new JTextField(100);
        field.setUI(new RoundedFieldUI());
        panel.add(field);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
