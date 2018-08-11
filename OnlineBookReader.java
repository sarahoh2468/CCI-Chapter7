import java.util.HashMap;

public class OnlineBookReader {
    public class Book {
        private int id;
        private String title;
        private String author;
        private int leftOff;

        public Book(int i, String t, String a) {
            this.id = i;
            this.title = t;
            this.author = a;
            this.leftOff = 0;
        }

        public int getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getAuthor() {
            return this.author;
        }

        public int getLeftOff() {
            return this.leftOff;
        }
    }

    public class User {
        private int id;

        public User(int i) {
            this.setId(i);
        }

        public int getUser() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

    }

    public class Display {
        private Book currentBook;
        private User currentUser;
        private int currentPage;

        public int displayUser(User u) {
            this.currentUser = u;
            return currentUser.getUser();
        }

        public int displayBookId(Book b) {
            this.currentBook = b;
            return b.getId();
        }

        public String displayBookTitle(Book b) {
            this.currentBook = b;
            return currentBook.getTitle();
        }

        public String displayBookAuthor(Book b) {
            this.currentBook = b;
            return currentBook.getAuthor();
        }

        public int displayCurrentPage() {
            this.currentPage = currentBook.getLeftOff();
            return currentPage;
        }
    }

    public class Library {
        private HashMap<Integer, Book> books;

        public Book addBook(int id, String title, String author) {
            Book book = new Book(id, title, author);
            if (books.containsKey(id)) {
                return null;
            }
            books.put(id, book);
            return book;
        }

        public boolean remove(Book b) {
            int i = b.getId();
            return remove(i);

        }

        public boolean remove(int id) {
            if (!books.containsKey(id)) {
                return false;
            }
            books.remove(id);
            return true;
        }

        public Book find(int id) {
            return books.get(id);
        }
    }
}
