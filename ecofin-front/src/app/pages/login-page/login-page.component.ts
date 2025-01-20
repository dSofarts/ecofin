import {Component, inject, signal} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Router} from "@angular/router";
import {LoginResponse} from "../../auth/auth.interface";

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgOptimizedImage
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {

  authService = inject(AuthService)
  router = inject(Router)

  expiredDate: Date | null = null
  tempToken: string | null = null
  operationId: string | null = null
  countdown: string = ''
  private intervalId: any
  private shouldContinue: boolean = true
  canTryAgain = signal<boolean>(false)

  loginForm = new FormGroup({
    phone: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required),
  })

  code: string[] = ['', '', '', '', '', ''];

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId)
    }
  }

  sendCode($event: any) {
    if (this.loginForm.valid) {
      const attemptSendCode = (attempt: number) => {
        if (attempt > 60) {
          console.error('Превышено количество попыток отправки кода.');
          return;
        }

        // @ts-ignore
        this.authService.sendCode(this.loginForm.value)
          .subscribe({
            next: (res) => {
              this.updateDataFromAuth(res);
            },
            error: (err) => {
              if (err.status === 403) {
                setTimeout(() => attemptSendCode(attempt + 1), 5000);
              } else {
                console.error('Ошибка при отправке кода:', err);
              }
            }
          });
      };
      attemptSendCode(1);
    }
  }

  login($event: any) {
    if (this.getCode().length == 6) {
      const payload = {
        otp: this.getCode(),
        operationId: this.operationId,
        tempToken: this.tempToken
      }
      // @ts-ignore
      this.authService.login(payload).subscribe(res => {
        this.router.navigate([''])
        console.log(res)
      })
    }
  }

  startCountdown(): void {
    this.updateCountdown();
    this.intervalId = setInterval(() => {
      this.updateCountdown();
    }, 1000);
  }

  updateCountdown(): void {
    const now = new Date().getTime()
    // @ts-ignore
    const target = new Date(this.expiredDate).getTime();
    const difference = target - now

    if (difference <= 0) {
      this.countdown = ''
      this.canTryAgain.set(true)
      clearInterval(this.intervalId)
      return
    }

    const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60))
    const seconds = Math.floor((difference % (1000 * 60)) / 1000)

    this.countdown = `${this.formatNumber(minutes)}:${this.formatNumber(seconds)}`
  }

  formatNumber(num: number): string {
    return num < 10 ? `0${num}` : `${num}`
  }

  onInput(event: Event, nextInput: HTMLInputElement | null): void {
    const inputElement = event.target as HTMLInputElement;
    const value = inputElement.value;

    const index = Array.from(inputElement.parentElement!.children).indexOf(inputElement);
    if (index !== -1) {
      this.code[index] = value;
    }

    if (value && nextInput) {
      nextInput.focus();
    }
    if (nextInput == null) {
      this.login(event)
    }
  }

  onBackspace(event: KeyboardEvent, previousInput: HTMLInputElement | null): void {
    const inputElement = event.target as HTMLInputElement;

    if (event.key === 'Backspace' && !inputElement.value && previousInput) {
      previousInput.focus();
    }
  }

  getCode(): string {
    return this.code.join('');
  }

  updateDataFromAuth(res: LoginResponse) {
    this.canTryAgain.set(false)
    this.expiredDate = res.expired
    this.tempToken = res.tempToken
    this.operationId = res.operationId
    clearInterval(this.intervalId)
    this.startCountdown()
  }

  onPaste(event: ClipboardEvent): void {
    event.preventDefault();

    const clipboardData = event.clipboardData;
    if (!clipboardData) return;

    const pastedData = clipboardData.getData('text');

    if (pastedData) {
      for (let i = 0; i < 6; i++) {
        this.code[i] = pastedData.charAt(i);
      }
      this.login(event)
    }
  }
}
