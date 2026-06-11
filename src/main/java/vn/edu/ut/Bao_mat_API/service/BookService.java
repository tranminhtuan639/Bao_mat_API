package vn.edu.ut.Bao_mat_API.service;

import vn.edu.ut.Bao_mat_API.dto.request.BookRequest;
import vn.edu.ut.Bao_mat_API.dto.response.BookResponse;
import vn.edu.ut.Bao_mat_API.entity.Book;
import vn.edu.ut.Bao_mat_API.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách id: " + id));
        return toResponse(book);
    }

    public BookResponse createBook(BookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .build();
        return toResponse(bookRepository.save(book));
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách id: " + id));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setImageUrl(request.getImageUrl());
        book.setDescription(request.getDescription());
        return toResponse(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .imageUrl(book.getImageUrl())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .build();
    }
}