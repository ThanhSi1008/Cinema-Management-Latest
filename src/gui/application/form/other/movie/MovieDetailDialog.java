package gui.application.form.other.movie;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.formdev.flatlaf.FlatClientProperties;

import entity.Movie;
import net.miginfocom.swing.MigLayout;

public class MovieDetailDialog extends JDialog {

	private JPanel container;
	private Movie movie;
	private JPanel leftContainer;
	private JPanel rightContainer;
	private JLabel movieName;
	private JLabel movieImage;
	private JPanel leftTopContainer;
	private JPanel leftBottomContainer;
	private JPanel durationContainer;
	private JLabel durationLabel;
	private JLabel duration;
	private JPanel rightMainContainer;
	private JPanel directorContainer;
	private JLabel directorLabel;
	private JLabel director;
	private JPanel statusContainer;
	private JLabel statusLabel;
	private JLabel status;
	private JPanel languageContainer;
	private JLabel languageLabel;
	private JLabel language;
	private JPanel countryContainer;
	private JLabel countryLabel;
	private JLabel country;
	private JPanel genreContainer;
	private JLabel genreLabel;
	private JLabel genre;
	private JPanel releasedDateContainer;
	private JLabel releasedDateLabel;
	private JLabel releasedDate;
	private JPanel startDateContainer;
	private JLabel startDateLabel;
	private JLabel startDate;
	private JPanel importPriceContainer;
	private JLabel importPriceLabel;
	private JLabel importPrice;
	private JPanel trailerContainer;
	private JLabel trailerLabel;
	private JLabel trailer;
	private JPanel descriptionContainer;
	private JLabel descriptionLabel;
	private JLabel description;
	private String labelStyles;

	public MovieDetailDialog(Movie movie) {
		this.movie = movie;
		setLayout(new MigLayout());
		container = new JPanel(new MigLayout("wrap, fill", "[fill][fill][fill]", "[fill]"));
		leftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		leftTopContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[fill]"));
		leftBottomContainer = new JPanel(new MigLayout("wrap, fillx", "[fill]", "[]"));
		movieName = new JLabel(movie.getMovieName());
		leftContainer.add(leftTopContainer);
		leftContainer.add(leftBottomContainer);

		leftTopContainer.add(movieName);
		
		ImageIcon icon = new ImageIcon(movie.getImageSource());
		if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
			icon = new ImageIcon("images/movie-poster-not-found.jpg");
		}
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImg);
		movieImage = new JLabel(resizedIcon);
		
		leftBottomContainer.add(movieImage);

		rightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));
		rightMainContainer = new JPanel(new MigLayout("wrap, fillx", "[][]", ""));
		rightContainer.add(rightMainContainer);

		durationContainer = new JPanel(new MigLayout("", "[][]", ""));
		durationLabel = new JLabel("Duration: ");
		duration = new JLabel(movie.getDuration() + "minute(s)");
		durationContainer.add(durationLabel);
		durationContainer.add(duration);

		directorContainer = new JPanel(new MigLayout("", "[][]", ""));
		directorLabel = new JLabel("Director: ");
		director = new JLabel(movie.getDirector());
		directorContainer.add(directorLabel);
		directorContainer.add(director);

		statusContainer = new JPanel(new MigLayout("", "[][]", ""));
		statusLabel = new JLabel("Status: ");
		status = new JLabel(movie.getStatus());
		statusContainer.add(statusLabel);
		statusContainer.add(status);

		languageContainer = new JPanel(new MigLayout("", "[][]", ""));
		languageLabel = new JLabel("Status: ");
		language = new JLabel(movie.getLanguage());
		languageContainer.add(languageLabel);
		languageContainer.add(language);

		countryContainer = new JPanel(new MigLayout("", "[][]", ""));
		countryLabel = new JLabel("Country: ");
		country = new JLabel(movie.getCountry());
		countryContainer.add(countryLabel);
		countryContainer.add(country);

		genreContainer = new JPanel(new MigLayout("", "[][]", ""));
		genreLabel = new JLabel("Genre: ");
		genre = new JLabel(movie.getGenre());
		genreContainer.add(genreLabel);
		genreContainer.add(genre);

		releasedDateContainer = new JPanel(new MigLayout("", "[][]", ""));
		releasedDateLabel = new JLabel("Released Year: ");
		releasedDate = new JLabel(movie.getReleasedDate().format(DateTimeFormatter.ofPattern("yyyy")));
		releasedDateContainer.add(releasedDateLabel);
		releasedDateContainer.add(releasedDate);

		startDateContainer = new JPanel(new MigLayout("", "[][]", ""));
		startDateLabel = new JLabel("Start Date: ");
		startDate = new JLabel(movie.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		startDateContainer.add(startDateLabel);
		startDateContainer.add(startDate);

		importPriceContainer = new JPanel(new MigLayout("", "[][]", ""));
		importPriceLabel = new JLabel("Import Price: ");
		importPrice = new JLabel("$" + new DecimalFormat("#0.00").format(movie.getImportPrice()));
		importPriceContainer.add(importPriceLabel);
		importPriceContainer.add(importPrice);

		trailerContainer = new JPanel(new MigLayout("", "[][]", ""));
		trailerLabel = new JLabel("Trailer: ");
		trailer = new JLabel(movie.getTrailer());
		trailerContainer.add(trailerLabel);
		trailerContainer.add(trailer);

		descriptionContainer = new JPanel(new MigLayout("wrap, fill", "[]", "[][]"));
		descriptionLabel = new JLabel("Description: ");
		description = new JLabel("<html>" + wrapTextEveryNWords(movie.getDescription(), 8).replaceAll("\\n", "<br>") + "</html>");
		descriptionContainer.add(descriptionLabel);
		descriptionContainer.add(description);

		rightMainContainer.add(durationContainer);
		rightMainContainer.add(directorContainer);
		rightMainContainer.add(statusContainer);
		rightMainContainer.add(languageContainer);
		rightMainContainer.add(countryContainer);
		rightMainContainer.add(genreContainer);
		rightMainContainer.add(releasedDateContainer);
		rightMainContainer.add(startDateContainer);
		rightMainContainer.add(importPriceContainer, "wrap");
		rightMainContainer.add(trailerContainer, "span 2");
		rightMainContainer.add(descriptionContainer);
		
		// styles
		movieName.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font; foreground:$primary");
		labelStyles = "font:$p.font";
		durationLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		directorLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		statusLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		languageLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		countryLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		genreLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		releasedDateLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		startDateLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		importPriceLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		trailerLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
		descriptionLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);

		container.add(leftContainer);
		container.add(rightContainer, "span 2");
		add(container, "dock center");
		pack();
		setLocationRelativeTo(null);
	}

    private static String wrapTextEveryNWords(String text, int n) {
        StringBuilder wrappedText = new StringBuilder();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            wrappedText.append(words[i]).append(" ");
            if ((i + 1) % n == 0) {
                wrappedText.append("\n");
            }
        }
        return wrappedText.toString().trim();
    }

}
