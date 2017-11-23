package it.unisa.codeSmellAnalyzer.computation;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;

import it.unisa.codeSmellAnalyzer.beans.ClassBean;
import it.unisa.codeSmellAnalyzer.beans.Entidade;
import it.unisa.codeSmellAnalyzer.beans.InstanceVariableBean;
import it.unisa.codeSmellAnalyzer.beans.MethodBean;
import it.unisa.codeSmellAnalyzer.beans.PackageBean;

public class RunDivergentChangeKNN {
	
	private static List<Entidade> entidades;

	public static void main(String args[]) {

		String pathToDirectory = "d:\\smell\\";

		File experimentDirectory = new File(pathToDirectory);

		for (File project : experimentDirectory.listFiles()) {
			try {
				// Method to convert a directory into a set of java packages.
				Vector<PackageBean> packages = FolderToJavaProjectConverter.convert(project.getAbsolutePath());
				List<ClassBean> classes = new ArrayList<>();
				Set<String> entidadesSet = new LinkedHashSet<>();
				for (PackageBean packageBean : packages) {
					classes.addAll(packageBean.getClasses());
				}
				for (ClassBean classBean : classes) {
					for (MethodBean m : classBean.getMethods()) {
						entidadesSet.add(m.getName());
					}
					for (InstanceVariableBean i : classBean.getInstanceVariables()) {
						entidadesSet.add(i.getName());
					}
				}

				for (ClassBean classBean : classes) {
					entidades.addAll(getEntidades(classBean, entidadesSet));
				}
				
				for (Entidade entidade: entidades) {
					for (Entidade e1: entidades) {
						if (!entidade.equals(e1)) {
							entidade.addDistancia(e1,calculaDistancia(entidade,e1));
						}
					}
				}
 
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private static Double calculaDistancia(Entidade entidade, Entidade e1) {
		Set<String> total = new LinkedHashSet<>();
		total.addAll(entidade.getDependencias());
		total.addAll(e1.getDependencias());
		Set<String> intersecao = new LinkedHashSet<>();
		for (String s: entidade.getDependencias()) {
			if (e1.getDependencias().contains(s)) {
				intersecao.add(s);
			}
		}
		return 1d - (new Double(intersecao.size()) / new Double(total.size()));
	}

	private static List<Entidade> getEntidades(ClassBean classBean,Set<String> entidadesSet) {
		List<Entidade> entidades = new ArrayList<Entidade>();
		for (InstanceVariableBean bean : classBean.getInstanceVariables()) {
			Entidade e = new Entidade(bean.getName(),classBean.getName());
			e.addDependencia(bean.getName());
			for (MethodBean m : classBean.getMethods()) {
				if (m.getUsedInstanceVariables().contains(bean)) {
					if (entidadesSet.contains(m.getName())) {
						e.addDependencia(m.getName());	
					}					
				}
			}
			entidades.add(e);
		}
		for (MethodBean m : classBean.getMethods()) {
			Entidade e = new Entidade(m.getName(),classBean.getName());
			e.addDependencia(m.getName());
			for (InstanceVariableBean i : m.getUsedInstanceVariables()) {
				if (entidadesSet.contains(i.getName())) {
					e.addDependencia(i.getName());	
				}
				
			}
			for (MethodBean m2 : m.getMethodCalls()) {
				if (entidadesSet.contains(m2.getName())) {
					e.addDependencia(m2.getName());
				}
			}
			for (MethodBean m1 : classBean.getMethods()) {
				if (m1.getMethodCalls().contains(m)) {
					e.addDependencia(m1.getName());
				}
			}
			entidades.add(e);
		}
		return entidades;
	}
}
