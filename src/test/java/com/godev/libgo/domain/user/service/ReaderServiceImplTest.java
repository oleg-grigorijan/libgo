package com.godev.libgo.domain.user.service;

import com.godev.libgo.MessageKeys;
import com.godev.libgo.domain.commons.exception.EntityNotFoundException;
import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.TxTemplateMock;
import com.godev.libgo.domain.commons.security.Auths;
import com.godev.libgo.domain.commons.security.exception.AuthorizationException;
import com.godev.libgo.domain.user.UserException;
import com.godev.libgo.domain.user.model.IdentityDocument;
import com.godev.libgo.domain.user.model.IdentityDocumentType;
import com.godev.libgo.domain.user.model.Reader;
import com.godev.libgo.domain.user.model.ReaderRegistrationRequest;
import com.godev.libgo.domain.user.model.UserRole;
import com.godev.libgo.domain.user.persistence.ReaderRepository;
import com.godev.libgo.infra.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderServiceImplTest {

    ReaderService readerService;
    @Mock ReaderRepository repository;
    @Mock SecurityContext security;

    static final String FULL_NAME = "Oleg Grigorijan";
    static final Email EMAIL = Email.of("oleg.grigorijan@gmail.com");
    static final IdentityDocument IDENTITY_DOCUMENT = IdentityDocument.of(IdentityDocumentType.PASSPORT, "NNNNNN");

    @BeforeEach
    void setUp() {
        readerService = new ReaderServiceImpl(repository, security, new TxTemplateMock());
    }

    @Test
    void readerShouldBeCreated() {
        var request = ReaderRegistrationRequest.of(FULL_NAME, EMAIL, IDENTITY_DOCUMENT);

        when(repository.existsByEmail(EMAIL)).thenReturn(false);

        Reader reader = readerService.register(request);

        assertThat(reader)
                .returns(FULL_NAME, from(Reader::getFullName))
                .returns(EMAIL, from(Reader::getEmail))
                .returns(IDENTITY_DOCUMENT, from(Reader::getIdentityDocument))
                .returns(false, from(Reader::isIdentityConfirmed))
                .returns(UserRole.READER, from(Reader::getRole))
                .extracting(Reader::getId).isNotNull();
        verify(repository).create(any());
    }

    @Test
    void readerShouldNotBeCreatedIfExistsByEmail() {
        var request = ReaderRegistrationRequest.of(FULL_NAME, EMAIL, IDENTITY_DOCUMENT);

        when(repository.existsByEmail(EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> readerService.register(request))
                .isInstanceOf(UserException.class)
                .hasMessage(MessageKeys.User.Error.ALREADY_EXISTS_BY_EMAIL);
        verify(repository, never()).create(any());
    }

    @Test
    void readerIdentityShouldBeConfirmed() {
        var id = randomUUID();
        var reader = new Reader(id, FULL_NAME, EMAIL, IDENTITY_DOCUMENT, false);

        when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(randomUUID()));
        when(repository.findById(id)).thenReturn(Optional.of(reader));
        readerService.confirmIdentity(id);

        verify(repository).update(argThat(Reader::isIdentityConfirmed));
    }

    @Test
    void readerIdentityShouldNotBeConfirmedIfNoAuthority() {
        when(security.getCurrentAuth()).thenReturn(Auths.guest());

        assertThatThrownBy(() -> readerService.confirmIdentity(randomUUID()))
                .isInstanceOf(AuthorizationException.class);
        verify(repository, never()).update(any());
    }

    @Test
    void getByIdShouldFailIfEntityNotFound() {
        var id = randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readerService.getById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getByEmailShouldFailIfEntityNotFound() {
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readerService.getByEmail(EMAIL))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
