package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceImplTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);
    }

    @Test
    void testRetrieveAllChambres() {
        Mockito.when(chambreRepository.findAll()).thenReturn(List.of(chambre));
        List<Chambre> chambres = chambreService.retrieveAllChambres();
        Assertions.assertNotNull(chambres);
        Assertions.assertEquals(1, chambres.size());
        Mockito.verify(chambreRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testRetrieveChambre() {
        Mockito.when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));
        Chambre foundChambre = chambreService.retrieveChambre(1L);
        Assertions.assertNotNull(foundChambre);
        Assertions.assertEquals(101, foundChambre.getNumeroChambre());
        Mockito.verify(chambreRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testAddChambre() {
        Mockito.when(chambreRepository.save(ArgumentMatchers.any(Chambre.class))).thenReturn(chambre);
        Chambre savedChambre = chambreService.addChambre(chambre);
        Assertions.assertNotNull(savedChambre);
        Assertions.assertEquals(101, savedChambre.getNumeroChambre());
        Mockito.verify(chambreRepository, Mockito.times(1)).save(chambre);
    }

    @Test
    void testModifyChambre() {
        chambre.setNumeroChambre(102);
        Mockito.when(chambreRepository.save(ArgumentMatchers.any(Chambre.class))).thenReturn(chambre);
        Chambre updatedChambre = chambreService.modifyChambre(chambre);
        Assertions.assertNotNull(updatedChambre);
        Assertions.assertEquals(102, updatedChambre.getNumeroChambre());
        Mockito.verify(chambreRepository, Mockito.times(1)).save(chambre);
    }

    @Test
    void testRemoveChambre() {
        Mockito.doNothing().when(chambreRepository).deleteById(1L);
        chambreService.removeChambre(1L);
        Mockito.verify(chambreRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testRecupererChambresSelonTyp() {
        Mockito.when(chambreRepository.findAllByTypeC(TypeChambre.SIMPLE)).thenReturn(List.of(chambre));
        List<Chambre> chambres = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);
        Assertions.assertNotNull(chambres);
        Assertions.assertEquals(1, chambres.size());
        Mockito.verify(chambreRepository, Mockito.times(1)).findAllByTypeC(TypeChambre.SIMPLE);
    }

    @Test
    void testTrouverchambreSelonEtudiant() {
        Mockito.when(chambreRepository.trouverChselonEt(12345678L)).thenReturn(chambre);
        Chambre chambreTrouvee = chambreService.trouverchambreSelonEtudiant(12345678L);
        Assertions.assertNotNull(chambreTrouvee);
        Assertions.assertEquals(101, chambreTrouvee.getNumeroChambre());
        Mockito.verify(chambreRepository, Mockito.times(1)).trouverChselonEt(12345678L);
    }
}