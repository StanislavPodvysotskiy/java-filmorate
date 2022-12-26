package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FilmStorageDB;
import ru.yandex.practicum.filmorate.dao.UserStorageDB;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmoRateApplicationTests {
	private UserStorage userStorage;
	private FilmStorage filmStorage;
	private final JdbcTemplate jdbcTemplate;

	private User user;
	private User friend;
	private Film film;

	@BeforeAll
	public void beforeAll() {
		userStorage = new UserStorageDB(jdbcTemplate);
		filmStorage = new FilmStorageDB(jdbcTemplate);
		user = new User();
		user.setName("Name");
		user.setLogin("Login");
		user.setEmail("test@mail.ru");
		user.setBirthday(LocalDate.of(1995, 12, 28));
		friend = new User();
		friend.setName("Friend");
		friend.setLogin("Friend");
		friend.setEmail("friend@mail.ru");
		friend.setBirthday(LocalDate.of(1991, 10, 10));
		userStorage.add(user);
		userStorage.add(friend);
		film = new Film();
		film.setName("Film");
		film.setDescription("Film");
		film.setDuration(120);
		film.setReleaseDate(LocalDate.of(1986, 12, 12));
		film.setRate(4);
		film.setMpaId(2);
		film.setMpaName("PG");
		film.setGenre(1, "Комедия");
		filmStorage.add(film);
	}

	@Test
	public void shouldBeUserId1() {
		User user = userStorage.getUserById(1);
		if (user == null) {
			throw new NotFoundException("User not found");
		}
		assertEquals(1, user.getId());
	}

	@Test
	public void shouldBeListSize2() {
		List<User> users = new ArrayList<>(userStorage.getUsers());
		assertEquals(2, users.size());
	}

	@Test
	public void shouldUpdateUser() {
		user = userStorage.getUserById(1);
		user.setEmail("new@mail.ru");
		userStorage.update(user);
		user = userStorage.getUserById(1);
		assertEquals("new@mail.ru", user.getEmail());
	}

	@Test
	public void shouldAddFriend() {
		user = userStorage.getUserById(1);
		friend = userStorage.getUserById(2);
		userStorage.addFriend(user.getId(), friend.getId());
		List<User> friends = userStorage.getAllFriends(user.getId());
		assertEquals(1, friends.size());
	}

	@Test
	public void shouldRemoveFriend() {
		User oneMoreUser = new User();
		oneMoreUser.setName("oneMoreUser");
		oneMoreUser.setLogin("oneMoreUser");
		oneMoreUser.setEmail("oneMoreUser@mail.ru");
		oneMoreUser.setBirthday(LocalDate.of(1971, 11, 23));
		oneMoreUser = userStorage.add(oneMoreUser);
		friend = userStorage.getUserById(2);
		userStorage.addFriend(oneMoreUser.getId(), friend.getId());
		userStorage.removeFriend(oneMoreUser.getId(), friend.getId());
		List<User> friends = userStorage.getAllFriends(oneMoreUser.getId());
		assertEquals(0, friends.size());
	}

	@Test
	public void shouldGetCommonFriend() {
		user = userStorage.getUserById(1);
		friend = userStorage.getUserById(2);
		User commonFriend = new User();
		commonFriend.setName("CommonFriend");
		commonFriend.setLogin("CommonFriend");
		commonFriend.setEmail("commonFriend@mail.ru");
		commonFriend.setBirthday(LocalDate.of(1981, 12, 12));
		commonFriend = userStorage.add(commonFriend);
		userStorage.addFriend(user.getId(), commonFriend.getId());
		userStorage.addFriend(friend.getId(), commonFriend.getId());
		List<User> commonFriends = userStorage.getCommonFriends(user.getId(), friend.getId());
		assertEquals(1, commonFriends.size());
	}

	@Test
	public void shouldBeFilmId1() {
		Film film = filmStorage.getFilmById(1);
		if (film == null) {
			throw new NotFoundException("Film not found");
		}
		assertEquals(1, film.getId());
	}

	@Test
	public void shouldBeListSize1() {
		List<Film> users = new ArrayList<>(filmStorage.getFilms());
		assertEquals(1, users.size());
	}

	@Test
	public void shouldUpdateFilm() {
		film = filmStorage.getFilmById(1);
		film.setDescription("New Description");
		filmStorage.update(film);
		film = filmStorage.getFilmById(1);
		assertEquals("New Description", film.getDescription());
	}

	@Test
	public void shouldBeRate5Than4() {
		film = filmStorage.getFilmById(1);
		filmStorage.addLike(1, 1);
		film = filmStorage.getFilmById(1);
		assertEquals(5, film.getRate());
		filmStorage.removeLike(1,1);
		film = filmStorage.getFilmById(1);
		assertEquals(4, film.getRate());
	}

	@Test
	public void shouldBeMpaListSize5() {
		List<Mpa> mpa = new ArrayList<>(filmStorage.getAllMpa());
		assertEquals(5, mpa.size());
	}

	@Test
	public void shouldBeMpaNameG() {
		Mpa mpa = filmStorage.getMpaById(1);
		assertEquals("G", mpa.getName());
	}

	@Test
	public void shouldBeGenreListSize6() {
		List<Genre> genres = new ArrayList<>(filmStorage.getAllGenres());
		assertEquals(6, genres.size());
	}

	@Test
	public void shouldBeGenreNameComedy() {
		Genre genre = filmStorage.getGenreById(1);
		assertEquals("Комедия", genre.getName());
	}

}
