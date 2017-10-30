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
import javax.swing.JCheckBox;
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
	private JCheckBox checkNome;
	private JLabel labelDir;
	private JLabel labelTag;
	private JLabel labelDirExport;
	private JLabel labelCheck;
	private JButton exibe;
	private File arquivos[];
	private File dir;
	private FileWriter result;
	private PrintWriter gerarResult;

	public Frame() {
		super("Leitor");
		setLayout(new FlowLayout());

		labelDir = new JLabel("Dir. arquivos");
		labelTag = new JLabel("Tag para localizar");
		labelDirExport = new JLabel("Resultado em");
		labelCheck = new JLabel("Imprime nome arquivo");
		checkNome = new JCheckBox();
		caixaDir = new JTextField(30);
		caixaRes = new JTextField(30);
		caixaTag = new JTextField(10);
		exibe = new JButton("Gerar");
		
		add(labelDir);
		add(caixaDir);
		add(labelTag);
		add(caixaTag);
		add(labelDirExport);
		add(caixaRes);
		add(labelCheck);
		add(checkNome);
		add(exibe);

		exibe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				if (evento.getSource() == exibe)
					ler();
			}
		});

		setVisible(true);
		setSize(400, 200);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
						trataElement(element, arquivos[i].getName(), checkNome.isSelected());
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

	private void trataElement(Element element, String nomeArquivo, Boolean imprimirNome) {

		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();

		while (it.hasNext()) {
			Element el = (Element) it.next();
			if (el.getName().toUpperCase()
					.equals(caixaTag.getText().isEmpty() ? "" : caixaTag.getText().toUpperCase())) {
				if (imprimirNome)
					gerarResult.printf(nomeArquivo + "   " + el.getText() + "%n");
				else
					gerarResult.printf(el.getText() + "%n");
			}
			trataElement(el, nomeArquivo, imprimirNome);
		}
	}

}
