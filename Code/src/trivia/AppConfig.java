/*
 * The MIT License
 *
 * Copyright 2015 Team Silent Coders.
 * Application developed for Amsterdam University of Applied Sciences and Amsta.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package trivia;

/**
 * Application configuration. Contains the default settings.
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class AppConfig {

	/**
	 * Application wide title.
	 */
	public static final String APPLICATION_NAME = "Amsta Triviant";

	/**
	 * Application copyright.
	 */
	public static final String APPLICATION_COPYRIGHT = "Copyright 2015 Amsta";

	/**
	 * Default value for timer.
	 */
	public static final Boolean TIMER_DEFAULT = true;

	/**
	 * Default value for short games.
	 */
	public static final Integer SHORT_LENGTH = 15;

	/**
	 * Default value for medium games.
	 */
	public static final Integer MEDIUM_LENGTH = 30;

	/**
	 * Default value for long games.
	 */
	public static final Integer LONG_LENGTH = 45;

	/**
	 * Default value for games.
	 */
	public static final Integer DEFAULT_LENGTH = SHORT_LENGTH;

	/**
	 * Minimum application width.
	 */
	public static final int MIN_WIDTH = 800;

	/**
	 * Minimum application height.
	 */
	public static final int MIN_HEIGHT = 600;

	/**
	 * Default database URL.
	 */
	public static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/trivia";

	/**
	 * Default database user.
	 */
	public static final String DEFAULT_USER = "root";

	/**
	 * Default database user password.
	 */
	public static final String DEFAULT_PASS = "root";

}
