import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { AvaliacaoService } from '../../avaliacao/service/avaliacao.service';
import { IViewAvaliacao } from '../view-avaliacao.model';

@Component({
  standalone: true,
  selector: 'jhi-avaliacao',
  templateUrl: './view-avaliacoes.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    HasAnyAuthorityDirective,
  ],
})
export class ViewAvaliacoesComponent implements OnInit {
  avaliacoes?: IViewAvaliacao[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected avaliacaoService: AvaliacaoService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.avaliacaoService.getViewAvaliacoes().subscribe(res => {
      this.avaliacoes = res.body;
    });
  }
}
