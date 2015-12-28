package wrappers;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class Song {

	public enum Genre {
		POP, ROCK, DANCE
	}

	public enum Tempo {
		FAST, MEDIUM, SLOW
	}

	private String name;
	private String artist;
	private Genre genre;
	private Tempo tempo;
	public int score;

	public Song() {

	}

	public Song(String name, String artist, Genre genre, Tempo tempo, int score) {
		super();
		this.name = name;
		this.artist = artist;
		this.genre = genre;
		this.tempo = tempo;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Tempo getTempo() {
		return tempo;
	}

	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Song [name=" + name + ", artist=" + artist + ", genre=" + genre + ", tempo=" + tempo + ", score="
				+ score + "]";
	}

	static Random random = new Random();

	public static Song generateRandomSong() {

		String songName = RandomStringUtils.random(4);
		String songArtist = RandomStringUtils.random(3);
		Genre songGenre = generateRandomGenre();
		Tempo songTempo = generateRandomTempo();
		int songScore = generateRandomScore();

		return new Song(songName, songArtist, songGenre, songTempo, songScore);
	}

	private static int generateRandomScore() {

		return random.nextInt(6) + 1;
	}

	private static Tempo generateRandomTempo() {

		int x = random.nextInt(3);
		if (x == 0) {
			return Tempo.FAST;
		}
		if (x == 1) {
			return Tempo.MEDIUM;
		}
		return Tempo.SLOW;
	}

	private static Genre generateRandomGenre() {
		int x = random.nextInt(3);
		if (x == 0) {
			return Genre.DANCE;
		}
		if (x == 1) {
			return Genre.POP;
		}
		return Genre.ROCK;
	}

	public static Song generateSongFromXls(double yearD, String categoryS, String titleS, String artistS,
			double energyD, String genreS, String tempoS, String genderS) {

		String _artist = artistS;
		String _title = titleS;

		Genre _genre;
		switch (genreS) {
		case "r":
			_genre = Genre.ROCK;
			break;
		case "R":
			_genre = Genre.ROCK;
			break;
		case "D":
			_genre = Genre.DANCE;
			break;
		case "d":
			_genre = Genre.DANCE;
			break;
		default:
			_genre = Genre.POP;
		}

		Tempo _tempo;
		switch (tempoS) {
		case "SS":
			_tempo = Tempo.SLOW;
			break;
		case "SM":
			_tempo = Tempo.SLOW;
			break;
		case "MM":
			_tempo = Tempo.MEDIUM;
			break;
		case "MF":
			_tempo = Tempo.MEDIUM;
			break;
		default:
			_tempo = Tempo.FAST;
		}
		int _score = generateRandomScore();
		

		Song song = new Song(_title, _artist, _genre, _tempo, _score);

		return song;
	}

}
