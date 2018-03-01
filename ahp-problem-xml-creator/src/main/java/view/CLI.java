package view;

import java.util.Scanner;

import model.AhpCore;
import model.Alternative;
import model.Criterion;
import model.NestedCriterion;
import model.SimpleCriterion;

public class CLI {
	private Scanner in = new Scanner(System.in);
	private AhpCore problem;

	public CLI() {
		chooseGoal();
		showAlternativesInterface();
		showAddCriteriaInterface();
		showComparsionValuesInterface();
		problem.processXML();
	}
	
	private void chooseGoal() {
		String goal;
		System.out.println("Jaki jest Twoj cel?");
		goal = in.nextLine();


		problem = AhpCore.getInstance();
		problem.setGoal(goal);
	}
	
	private void showAlternativesInterface() {
		String command;
		Integer id = 0;
		String name;
		do {
			System.out.println("Dodawanie nowej alternatywy: [e aby zakonczyc, a aby anulowac]");
			System.out.print("Nazwa: ");
			name = in.nextLine();
			
			if ( !(name.equals("e") || name.equals("a")) ) {
				problem.addAlternative(new Alternative(name, id));
				System.out.print("Dodano: "+name+"("); System.out.print(id);System.out.println(")");
				id++;
			}
		} while ( !(name.equals("e") || name.equals("a")) );
		
		if (name.equals("a")) {
			System.out.println("Anulowano.");
			System.exit(-1);
		}
		
		System.out.println("Zakonczono wprowadzanie alternatyw. Wyswietlic? [T/n]");

		command = in.nextLine();
		
		if ( command.equals("t") || command.equals("T") ) {
			System.out.println(problem.getAlternatives());
		}
	}

	private void showAddCriteriaInterface() {
		String type, name;
		Integer id = 1, parentId = 0;
		do {
			System.out.println("#########################################");
			System.out.println(problem.getRootCriterion().getSubcriteria());
			System.out.println("#########################################");
			System.out.println("Dodawanie nowego kryterium");
			System.out.println("Nazwa: ");
			name = in.nextLine();
			System.out.println("Typ: [c - proste kryterium, n - kryterium z podkryteriami, e - zako?cz,  a - anuluj, i - przykladowe kryteria");
			type = in.nextLine();
			if (type.equals("e") || type.equals("a") || type.equals("i")) break;
			System.out.println("Rodzic: [0 dla kryteriow najwyzszego poziomu]");
			parentId = in.nextInt();
			in.nextLine();
			
			if (type.equals("c")) {
				SimpleCriterion criterion = new SimpleCriterion(name, id);
				problem.addCriterion(criterion, parentId);
			} else if (type.equals("n")) {
				NestedCriterion nested = new NestedCriterion(name, id);
				problem.addCriterion(nested, parentId);
			}
			
			id++;
		} while ( !(type.equals("e") || type.equals("a") || type.equals("i")) );

		if (type.equals("a")) {
			System.out.println("Anulowano.");
			System.exit(-1);
		}
		
		if (type.equals("i"))
			addExampleValues();
		
		System.out.println(problem.getRootCriterion().getSubcriteria());
		problem.getRootCriterion().init(problem.getAlternatives().size());
		
	}
	
	private void addExampleValues() {
		problem.addAlternative(new Alternative("Turcja", 0));
		problem.addAlternative(new Alternative("Grecja", 0));
		problem.addAlternative(new Alternative("Hiszpania", 0));
		problem.addAlternative(new Alternative("Egipt", 0));

		SimpleCriterion c1 = new SimpleCriterion("Zabytki",1);
		NestedCriterion n1 = new NestedCriterion("Cena", 2);
		SimpleCriterion c2 = new SimpleCriterion("Jezyk", 3);
		NestedCriterion n2 = new NestedCriterion("Klimat", 4);
		
		SimpleCriterion c3 = new SimpleCriterion("Cena noclegu", 5);
		c3.den = 1;
		SimpleCriterion c4 = new SimpleCriterion("Cena jedzenia", 6);
		c4.den = 1;
		SimpleCriterion c5 = new SimpleCriterion("Cena atrakcji", 7);
		c5.den = 1;
		
		NestedCriterion n3 = new NestedCriterion("Temperatura", 8);
		n3.den = 1;
		
		SimpleCriterion c6 = new SimpleCriterion("Temperatura w dzien", 9);
		c6.den = 2;
		SimpleCriterion c7 = new SimpleCriterion("Temperatura w nocy", 10);
		c7.den = 2;
		
		SimpleCriterion c8 = new SimpleCriterion("Pogoda", 11);
		c8.den = 1;
		SimpleCriterion c9 = new SimpleCriterion("Wilgotnosc", 12);
		c9.den = 1;
		
		n1.addSubcriterion(c3);
		n1.addSubcriterion(c4);
		n1.addSubcriterion(c5);
		
		n3.addSubcriterion(c6);
		n3.addSubcriterion(c7);
		
		n2.addSubcriterion(n3);
		n2.addSubcriterion(c8);
		n2.addSubcriterion(c9);

		problem.getRootCriterion().addSubcriterion(c1);
		problem.getRootCriterion().addSubcriterion(n1);
		problem.getRootCriterion().addSubcriterion(c2);
		problem.getRootCriterion().addSubcriterion(n2);
	}

	private void showComparsionValuesInterface() {
		//System.out.println("interface");
		showCriteria(problem.getRootCriterion());
	}
	
	private void showCriteria(Criterion parent) {
		getSingleComparsion(parent);
		if (parent != null && parent.getSubcriteria() != null) {
			for (int i = 0; i < parent.getSubcriteria().size(); i++) {
				showCriteria(parent.getSubcriteria().get(i));
			}
		}

	}
	
	private void getSingleComparsion(Criterion c) {
		if (c.getClass().toString().equals("class model.SimpleCriterion") ) {
			System.out.println("Porownaj teraz parami wszystkie alternatywy pod wzgledem kryterium "+c.getName()+". [W ka?dej parze wybierz t? alternatyw?, kt?ra pod wzgl?dem podanego kryterium jest lepsza i zadecyduj o ile razy jest lepsza.");
			for (int i = 1; i <= c.getComparsionsCount() ; i++) {
				do {
					System.out.println("O ile "+problem.getAlternatives().get(c.getSingleComparsionCoords(i).x).getName()+" jest lepsze od "+problem.getAlternatives().get(c.getSingleComparsionCoords(i).y).getName()+" pod wzgl?dem "+c.getName()+"?");

					Double comparsionValue = Double.parseDouble(in.nextLine());
					c.setSingleValue(i, comparsionValue);
					if (!c.isConsistent()) {
						System.out.println("Ten wyb?r nie jest sp?jny z pozosta?ymi wyborami. CR: "+c.getConsistencyRatio()+">0.1");
						System.out.println("[i - zignoruj do czasu uzupe?nienia ca?ej macierzy, r - popraw");
						String cmd = in.nextLine();
						if (cmd.equals("i")) break;
					} else {
						System.out.println("CR: "+c.getConsistencyRatio());
					}
				} while(!c.isConsistent());
				if ( i == c.getComparsionsCount() && !c.isConsistent() ) {
					System.out.println("Macierz nie jest spojna, nalezy uzupelnic od nowa");
					i = 0;
				}
			}
			System.out.println();

		} else {
			System.out.println("Porownaj teraz parami kryteria "+c.toStringList()+ ". [W ka?dej parze wybierz to kryterium, kt?re jest dla Ciebie istotniejsze i okre?l ile razy jest istotniejsze.");
			for (int i = 1; i <= c.getComparsionsCount() ; i++) {
				do {
					System.out.println("O ile kryterium "+c.getSubcriteria().get(c.getSingleComparsionCoords(i).x).getName()+" jest dla Ciebie istotniejsze od "+c.getSubcriteria().get(c.getSingleComparsionCoords(i).y).getName()+"?");

					Double comparsionValue = Double.parseDouble(in.nextLine());
					c.setSingleValue(i, comparsionValue);
					if (!c.isConsistent()) {
						System.out.println("Ten wyb?r nie jest sp?jny z pozosta?ymi wyborami. CR: "+c.getConsistencyRatio());
						System.out.println("[i - zignoruj do czasu uzupe?nienia ca?ej macierzy, r - popraw");
						String cmd = in.nextLine();
						if (cmd.equals("i")) break;
					} else {
						System.out.println("CR: "+c.getConsistencyRatio());
					}
				} while(!c.isConsistent());
				if ( i == c.getComparsionsCount() && !c.isConsistent() ) {
					System.out.println("Macierz nie jest spojna, nalezy uzupelnic od nowa");
					i = 0;
				}
			}
			System.out.println();
		}
	}
}
