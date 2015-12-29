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
	public enum Energy{
		SUICIDE, SAD, NORMAL, HAPPY
	}
	public enum Gender{
		M, W
	}

	private String name;
	private Gender gender;
	private String artist;
	private Energy energy;
	private Genre genre;
	private Tempo tempo;
	public int score;

	public Song() {

	}

	public Song(String name, String artist, Genre genre, Tempo tempo, Energy energy, Gender gender, int score) {
		super();
		this.name = name;
		this.artist = artist;
		this.genre = genre;
		this.tempo = tempo;
		this.score = score;
		this.energy = energy;
		this.gender = gender;
	}
	
	

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Energy getEnergy() {
		return energy;
	}

	public void setEnergy(Energy energy) {
		this.energy = energy;
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
		return "Song [name=" + name + ", gender=" + gender + ", artist=" + artist + ", energy=" + energy + ", genre="
				+ genre + ", tempo=" + tempo + ", score=" + score + "]";
	}







	static Random random = new Random();

	public static Song generateRandomSong() {

		String songName = RandomStringUtils.random(4);
		String songArtist = RandomStringUtils.random(3);
		Genre songGenre = generateRandomGenre();
		Tempo songTempo = generateRandomTempo();
		Energy energy = generateRandomEnergy();
		Gender gender = generateRandomGender();
		int songScore = generateRandomScore();

		return new Song(songName, songArtist, songGenre, songTempo, energy, gender, songScore);
	}

	private static Gender generateRandomGender() {
		int x = random.nextInt(2);
		if (x == 0) {
			return Gender.M;
		}
		
		return Gender.W;
	}

	private static Energy generateRandomEnergy() {
		int x = random.nextInt(3);
		if (x == 0) {
			return Energy.SAD;
		}
		if (x == 1) {
			return Energy.NORMAL;
		}
		if (x == 2) {
			return Energy.SUICIDE;
		}
		return Energy.HAPPY;
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
		Energy _energy = Energy.HAPPY;
		if(energyD == 1){
			_energy = Energy.SUICIDE;
		}
		if(energyD == 2){
			_energy = Energy.SAD;
		}
		if(energyD == 3){
			_energy = Energy.NORMAL;
		}
		Gender _gender = Gender.M;
		if(genderS.equals("W")){
			_gender = Gender.W;
		}
		
		int _score = generateRandomScore();

		Song song = new Song(_title, _artist, _genre, _tempo, _energy, _gender,_score);

		return song;
	}

}
