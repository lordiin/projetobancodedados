import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Imagem } from '../../shared/imagens/imagem.model';

const initialAccount: Account = {} as Account;

@Component({
  selector: 'jhi-settings',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, SharedModule, SharedModule, SharedModule],
  templateUrl: './settings.component.html',
})
export default class SettingsComponent implements OnInit {
  success = false;
  imagem: Imagem = new Imagem();

  settingsForm = new FormGroup({
    nome: new FormControl(initialAccount.nome, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    sobrenome: new FormControl(initialAccount.sobrenome, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),

    activated: new FormControl(initialAccount.activated, { nonNullable: true }),
    id: new FormControl(initialAccount.id, { nonNullable: false }),
    authorities: new FormControl(initialAccount.authorities, { nonNullable: true }),
    matricula: new FormControl(initialAccount.matricula, { nonNullable: true }),
    imagem: new FormControl(initialAccount.imagem, { nonNullable: true }),
    imagemContentType: new FormControl(initialAccount.imagemContentType, { nonNullable: true }),
  });

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue(account);
        this.imagem.arquivo = account.imagem;
        this.imagem.contentType = account.imagemContentType;
      }
    });
  }

  save(): void {
    this.success = false;

    const account = this.settingsForm.getRawValue();
    account.imagem = this.imagem.arquivo;
    account.imagemContentType = this.imagem.contentType;
    this.accountService.save(account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(account);
    });
  }
}
