package com.example.jeddit.serviciesTests;

import com.example.jeddit.JedditApplication;
import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.commentaries.CommentaryCreateRequest;
import com.example.jeddit.models.models.commentaries.CommentaryUpdateRequest;
import com.example.jeddit.repositories.CommentaryRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.impl.CommentaryServiceImpl;
import com.example.jeddit.servicies.impl.JWTService;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JedditApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommentaryTests {

    @InjectMocks
    private CommentaryServiceImpl commentaryService;

    @Mock
    private CommentaryRepository commentaryRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Test
    public void create_Positive() throws DataNotFoundException, NotValidToken, NotCorrectDataException {
        long id = 1;
        String jwt = "jwt";

        CommentaryCreateRequest request = new CommentaryCreateRequest(jwt, "Text");

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(postRepository.findById(id)).thenReturn(Optional.of(new Post()));
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        commentaryService.create(id, request);

        verify(commentaryRepository, atLeastOnce()).save(any());
    }

    @Test
    public void create_notValidToken_throwsException() throws DataNotFoundException, NotValidToken, NotCorrectDataException {
        long id = 1;
        String jwt = "jwt";

        CommentaryCreateRequest request = new CommentaryCreateRequest(jwt, "Text");

        when(jwtService.validateToken(jwt)).thenReturn(false);

        assertThrows(NotValidToken.class, () -> commentaryService.create(id, request));
    }

    @Test
    public void create_postNotFound_throwsException() throws DataNotFoundException, NotValidToken, NotCorrectDataException {
        long id = 1;
        String jwt = "jwt";

        CommentaryCreateRequest request = new CommentaryCreateRequest(jwt, "Text");

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(postRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class, () -> commentaryService.create(id, request));
    }

    @Test
    public void create_userNotFound_throwsException() throws DataNotFoundException, NotValidToken, NotCorrectDataException {
        long id = 1;
        String jwt = "jwt";

        CommentaryCreateRequest request = new CommentaryCreateRequest(jwt, "Text");

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(postRepository.findById(id)).thenReturn(Optional.of(new Post()));
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class, () -> commentaryService.create(id, request));
    }

    @Test
    public void create_notCorrectData_throwsException() throws DataNotFoundException, NotValidToken, NotCorrectDataException {
        long id = 1;
        String jwt = "jwt";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 40001; i++) sb.append("s");

        CommentaryCreateRequest request = new CommentaryCreateRequest(jwt, sb.toString());

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(postRepository.findById(id)).thenReturn(Optional.of(new Post()));
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        assertThrows(NotCorrectDataException.class, () -> commentaryService.create(id, request));
    }

    @Test
    public void get_Positive() throws DataNotFoundException {
        long id = 1;
        Optional<Commentary> commentary = Optional.of(new Commentary());
        when(commentaryRepository.findById(id)).thenReturn(commentary);

        Commentary result = commentaryService.get(id);

        assertEquals(commentary.get(), result);
    }

    @Test
    public void get_dataNotFound_throwException() throws DataNotFoundException {
        long id = 1;
        Optional<Commentary> commentary = Optional.of(new Commentary());
        when(commentaryRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class, () -> commentaryService.get(id));
    }

    @Test
    public void update_Positive() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";
        CommentaryUpdateRequest request = new CommentaryUpdateRequest(jwt, "Text");

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(commentaryRepository.findById(id)).thenReturn(Optional.of(commentary));
        when(jwtService.validateToken(request.getJwttoken())).thenReturn(true);
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);

        commentaryService.update(id, request);

        verify(commentaryRepository).save(any());
    }

    @Test
    public void update_dataNotFound_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";
        CommentaryUpdateRequest request = new CommentaryUpdateRequest(jwt, "Text");

        when(commentaryRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class, () -> commentaryService.update(id, request));
    }

    @Test
    public void update_notValidToken_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";
        CommentaryUpdateRequest request = new CommentaryUpdateRequest(jwt, "Text");

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(commentaryRepository.findById(id)).thenReturn(Optional.of(commentary));
        when(jwtService.validateToken(request.getJwttoken())).thenReturn(false);

        assertThrows(NotValidToken.class, () -> commentaryService.update(id, request));
    }

    @Test
    public void update_notEnoughRights_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";
        CommentaryUpdateRequest request = new CommentaryUpdateRequest(jwt, "Text");

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(commentaryRepository.findById(id)).thenReturn(Optional.of(commentary));
        when(jwtService.validateToken(request.getJwttoken())).thenReturn(true);
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(10L);

        assertThrows(NotEnoughRightsException.class, () -> commentaryService.update(id, request));
    }

    @Test
    public void delete_Positive() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";

        JWTTokenRequest request = new JWTTokenRequest(jwt);

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(commentaryRepository.findById(id)).thenReturn(Optional.of(commentary));
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        commentaryService.delete(id, request);

        verify(commentaryRepository, atLeastOnce()).delete(any());
    }

    @Test
    public void delete_notValidToken_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";

        JWTTokenRequest request = new JWTTokenRequest(jwt);

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(jwtService.validateToken(jwt)).thenReturn(false);
        assertThrows(NotValidToken.class,() -> commentaryService.delete(id, request));
    }

    @Test
    public void delete_dataNotFound_1_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";

        JWTTokenRequest request = new JWTTokenRequest(jwt);

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(commentaryRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class,() -> commentaryService.delete(id, request));
    }

    @Test
    public void delete_dataNotFound_2_throwException() throws DataNotFoundException, NotValidToken, NotEnoughRightsException {
        long id = 1;
        String jwt = "jwt";

        JWTTokenRequest request = new JWTTokenRequest(jwt);

        User user = new User();
        user.setId(1L);

        Commentary commentary = new Commentary();
        commentary.setOwner(user);

        when(jwtService.validateToken(jwt)).thenReturn(true);
        when(commentaryRepository.findById(id)).thenReturn(Optional.of(commentary));
        when(jwtService.extractUserId(request.getJwttoken())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(DataNotFoundException.class,() -> commentaryService.delete(id, request));
    }
}
