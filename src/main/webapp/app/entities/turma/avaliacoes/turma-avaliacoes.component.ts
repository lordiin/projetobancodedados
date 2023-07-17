import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITurma } from '../turma.model';
import { IAvaliacao } from '../../avaliacao/avaliacao.model';
import { AvaliacaoService } from '../../avaliacao/service/avaliacao.service';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { AccountService } from '../../../core/auth/account.service';
import { Account } from '../../../core/auth/account.model';

@Component({
  standalone: true,
  selector: 'jhi-turma-avaliacoes-detail',
  templateUrl: './turma-avaliacoes.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TurmaAvaliacoesComponent implements OnInit {
  @Input() turma: ITurma | null = null;
  avaliacoes: IAvaliacao[] | null = null;
  faStar = faStar;
  currentAccount: Account;
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected avaliacaoService: AvaliacaoService,
    private router: Router,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
  }

  loadAll() {
    this.avaliacaoService.findAllByTurmaId(this.turma.id).subscribe(res => {
      this.avaliacoes = res.body;
    });
  }

  navigateCriarAvaliacao() {
    this.router.navigate(['/avaliacao/new/turma/', this.turma.id]);
  }

  navigateDenunciarAvaliacao(avaliacaoId: number) {
    const a = this.router.navigate(['/avaliacao', avaliacaoId, 'denuncia']);
  }

  deletarAvaliacao(avaliacaoId: number) {
    this.avaliacaoService.delete(avaliacaoId).subscribe(() => {
      this.loadAll();
    });
  }

  previousState(): void {
    window.history.back();
  }
}
