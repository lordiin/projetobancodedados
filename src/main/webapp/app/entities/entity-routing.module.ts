import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'avaliacao',
        data: { pageTitle: 'Avaliações' },
        loadChildren: () => import('./avaliacao/avaliacao.routes'),
      },
      {
        path: 'denuncia',
        data: { pageTitle: 'Denúncias' },
        loadChildren: () => import('./denuncia/denuncia.routes'),
      },
      {
        path: 'departamento',
        data: { pageTitle: 'Departamentos' },
        loadChildren: () => import('./departamento/departamento.routes'),
      },
      {
        path: 'disciplina',
        data: { pageTitle: 'Disciplinas' },
        loadChildren: () => import('./disciplina/disciplina.routes'),
      },
      {
        path: 'professor',
        data: { pageTitle: 'Professores' },
        loadChildren: () => import('./professor/professor.routes'),
      },
      {
        path: 'turma',
        data: { pageTitle: 'Turmas' },
        loadChildren: () => import('./turma/turma.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
