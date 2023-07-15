// import { Component, OnInit, OnDestroy } from '@angular/core';
// import { Router, RouterModule } from '@angular/router';
// import { Subject } from 'rxjs';
// import { takeUntil } from 'rxjs/operators';
//
// import SharedModule from 'app/shared/shared.module';
// import { AccountService } from 'app/core/auth/account.service';
// import { Account } from 'app/core/auth/account.model';
// import LoginComponent from "../login/login.component";
//
// @Component({
//   standalone: true,
//   selector: 'jhi-home',
//   templateUrl: './home.component.html',
//   styleUrls: ['./home.component.scss'],
//   imports: [SharedModule, RouterModule, LoginComponent],
// })
// export default class HomeComponent implements OnInit, OnDestroy {
//   account: Account | null = null;
//
//   private readonly destroy$ = new Subject<void>();
//
//   constructor(private accountService: AccountService, private router: Router) {}
//
//   ngOnInit(): void {
//     // this.accountService
//     //   .getAuthenticationState()
//     //   .pipe(takeUntil(this.destroy$))
//     //   .subscribe(account => (this.account = account));
//   }
//
//   login(): void {
//     this.router.navigate(['/login']);
//   }
//
//   ngOnDestroy(): void {
//     this.destroy$.next();
//     this.destroy$.complete();
//   }
// }
import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { AvaliacaoComponent } from '../entities/avaliacao/list/avaliacao.component';

@Component({
  selector: 'jhi-home',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule, AvaliacaoComponent],
  templateUrl: './home.component.html',
})
export default class HomeComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;
  logado = false;

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });

  constructor(private accountService: AccountService, private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
        this.logado = true;
      }
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  login(): void {
    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['/turma']);
        }
      },
      error: () => (this.authenticationError = true),
    });
  }
}
