import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IAvaliacao } from '../avaliacao.model';
import { AvaliacaoService } from '../service/avaliacao.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './avaliacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AvaliacaoDeleteDialogComponent {
  avaliacao?: IAvaliacao;

  constructor(protected avaliacaoService: AvaliacaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.avaliacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
