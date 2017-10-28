package com.xml.model;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JTextField caixaDir;
	private JTextField caixaTag;
	private JTextField caixaRes;
	private JLabel labelDir;
	private JLabel labelTag;
	private JLabel labelDirExport;
	private JButton exibe;
	private File arquivos[];
	private File dir;
	private FileWriter result;
	private PrintWriter gerarResult;

	public Frame() {
		super("Leitor");
		setLayout(new FlowLayout());

		labelDir = new JLabel("Diretório dos arquivos");
		labelTag = new JLabel("Tag para localizar");
		labelDirExport = new JLabel("Gerar resultado em");
		caixaDir = new JTextField(30);
		caixaRes = new JTextField(30);
		caixaTag = new JTextField(10);
		add(labelDir);
		add(caixaDir);
		add(labelTag);
		add(caixaTag);
		add(labelDirExport);
		add(caixaRes);

		exibe = new JButton("Gerar");
		add(exibe);

		exibe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				if (evento.getSource() == exibe)
					ler();
			}
		});

		this.setVisible(true);
		this.setSize(400, 150);
		pack();
	}

	private void ler() {
		if (!caixaDir.getText().isEmpty() && !caixaRes.getText().isEmpty()) {
			dir = new File(caixaDir.getText());
			arquivos = dir.listFiles();

			try {
				result = new FileWriter(caixaRes.getText());
				gerarResult = new PrintWriter(result);
				gerarResult.printf(
						"Encontrados " + arquivos.length + " arquivos no diretório " + caixaDir.getText() + "%n");
				gerarResult.printf("Listando o conteúdo da tag " + caixaTag.getText() + "%n %n");
			} catch (Exception e) {
			}
			for (int i = 0; i < arquivos.length; i++) {
				SAXBuilder sb = new SAXBuilder();

				Document d;
				try {
					d = sb.build(arquivos[i]);
					Element nfe = d.getRootElement();

					List<Element> elements = nfe.getChildren();
					Iterator<Element> j = elements.iterator();

					while (j.hasNext()) {
						Element element = (Element) j.next();
						trataElement(element);
					}

				} catch (Exception ex) {
				}
			}
			try {
				gerarResult.printf("%nGeração Finalizada.");
				result.close();
			} catch (IOException e) {
			}
		}
	}

	private void trataElement(Element element) {

		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();

		while (it.hasNext()) {
			Element el = (Element) it.next();
			if (el.getName().toUpperCase()
					.equals(caixaTag.getText().isEmpty() ? "" : caixaTag.getText().toUpperCase())) {
				gerarResult.printf(el.getText() + "%n");
			}
			trataElement(el);
		}
	}

}
