package gui.application.form.other.movie;

import javax.swing.JDialog;
import javax.swing.JPanel;

import entity.Movie;
import net.miginfocom.swing.MigLayout;

public class MovieDetailDialog extends JDialog {

	private JPanel container;
	private Movie movie;

	public MovieDetailDialog(Movie movie) {
		this.movie = movie;
		setLayout(new MigLayout());
		container = new JPanel(new MigLayout("wrap, fill", "[][]", "[fill]"));
		
		add(container, "dock center");
		pack();
		setLocationRelativeTo(null);
	}
	
}
