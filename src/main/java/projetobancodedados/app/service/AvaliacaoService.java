package projetobancodedados.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Avaliacao;
import projetobancodedados.app.domain.NotaMediaTurmas;
import projetobancodedados.app.domain.User;
import projetobancodedados.app.domain.ViewAvaliacaoUsuario;
import projetobancodedados.app.repository.AvaliacaoRepository;
import projetobancodedados.app.repository.UserRepository;
import projetobancodedados.app.security.SecurityUtils;

/**
 * Service Implementation for managing {@link Avaliacao}.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AvaliacaoService {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoService.class);

    private final AvaliacaoRepository avaliacaoRepository;

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a avaliacao.
     *
     * @param avaliacao the entity to save.
     * @return the persisted entity.
     */
    public void save(Avaliacao avaliacao) {
        log.debug("Request to save Avaliacao : {}", avaliacao);
        Optional<User> user = userRepository.findOneByMatricula(SecurityUtils.getCurrentUserLogin().get());
        avaliacaoRepository.save(avaliacao.getDescricao(), avaliacao.getNota(), avaliacao.getTurma().getId(), user.get().getId());
    }

    /**
     * Update a avaliacao.
     *
     * @param avaliacao the entity to save.
     * @return the persisted entity.
     */
    public void update(Avaliacao avaliacao) {
        log.debug("Request to update Avaliacao : {}", avaliacao);
        avaliacaoRepository.update(avaliacao.getId(), avaliacao.getDescricao(), avaliacao.getNota(), avaliacao.getTurma().getId());
    }

    /**
     * Get all the avaliacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Avaliacao> findAll(Pageable pageable) {
        log.debug("Request to get all Avaliacaos");
        Page avaliacoes = avaliacaoRepository.findAll(pageable);
        avaliacoes.getContent().forEach(avaliacao -> log.info(avaliacao.toString()));
        return avaliacoes;
    }

    /**
     * Get one avaliacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Avaliacao findOne(Long id) {
        log.debug("Request to get Avaliacao : {}", id);
        Avaliacao avaliacao = avaliacaoRepository.findOne(id);
        log.info(avaliacao.toString());
        return avaliacao;
    }

    /**
     * Delete the avaliacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Avaliacao : {}", id);
        avaliacaoRepository.delete(id);
    }

    public List<Avaliacao> findAllByTurma(Long turmaId) {
        return avaliacaoRepository.findAllByTurmaId(turmaId);
    }

    public List<NotaMediaTurmas> getNotaMediaTurmas() {
        String nativeQuery = "SELECT * FROM obter_nota_media_turma()";
        Query query = entityManager.createNativeQuery(nativeQuery);
        List<Object[]> resultList = query.getResultList();
        List<NotaMediaTurmas> notaMediaTurmasList = new ArrayList<>();
        for (Object[] result : resultList) {
            NotaMediaTurmas notaMediaTurmas = new NotaMediaTurmas();
            notaMediaTurmas.setNomeDisciplina((String) result[0]);
            notaMediaTurmas.setNomeProfessor((String) result[1]);
            notaMediaTurmas.setNotaMedia((BigDecimal) result[2]);
            notaMediaTurmasList.add(notaMediaTurmas);
        }
        return notaMediaTurmasList;
    }

    public List<ViewAvaliacaoUsuario> getAvaliacoesUsuario() {
        String nativeQuery = "SELECT * FROM avaliacoes_por_usuario";
        Query query = entityManager.createNativeQuery(nativeQuery);
        List<Object[]> resultList = query.getResultList();
        List<ViewAvaliacaoUsuario> avaliacoesUsuarioList = new ArrayList<>();
        for (Object[] result : resultList) {
            ViewAvaliacaoUsuario avaliacaoUsuario = new ViewAvaliacaoUsuario();
            avaliacaoUsuario.setUserId((Integer) result[0]);
            avaliacaoUsuario.setUserMatricula((String) result[1]);
            avaliacaoUsuario.setUserNome((String) result[2]);
            avaliacaoUsuario.setUserSobrenome((String) result[3]);
            avaliacaoUsuario.setAvaliacaoId((Integer) result[4]);
            avaliacaoUsuario.setAvaliacaoDescricao((String) result[5]);
            avaliacaoUsuario.setAvaliacaoNota((Integer) result[6]);
            avaliacoesUsuarioList.add(avaliacaoUsuario);
        }
        return avaliacoesUsuarioList;
    }

    public Avaliacao createAvaliacao(Avaliacao avaliacao, Long turmaId) {
        log.debug("Request to save Avaliacao : {}", avaliacao);
        Long userId = userRepository.findUsuarioByMatricula(SecurityUtils.getCurrentUserLogin().get());
        avaliacaoRepository.save(avaliacao.getDescricao(), avaliacao.getNota(), turmaId, userId);
        return avaliacao;
    }
}
