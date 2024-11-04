package tn.esprit.tpfoyer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc(1L, "Bloc A", 1000L, null, new HashSet<>()));
        blocs.add(new Bloc(2L, "Bloc B", 1200L, null, new HashSet<>()));

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveAllBlocs();

        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testAddBloc() {
        Bloc bloc = new Bloc(1L, "Bloc A", 1000L, null, new HashSet<>());
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRemoveBloc() {
        Long blocId = 1L;

        doNothing().when(blocRepository).deleteById(blocId);

        blocService.removeBloc(blocId);

        verify(blocRepository, times(1)).deleteById(blocId);
    }

    @Test
    void testRetrieveBloc() {
        Long blocId = 1L;
        Bloc bloc = new Bloc(blocId, "Bloc A", 1000L, null, new HashSet<>());

        when(blocRepository.findById(blocId)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.retrieveBloc(blocId);

        assertNotNull(result);
        assertEquals(blocId, result.getIdBloc());
        verify(blocRepository, times(1)).findById(blocId);
    }


}
