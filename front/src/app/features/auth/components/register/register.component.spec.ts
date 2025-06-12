import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let routerMock: Partial<Router>;

  beforeEach(async () => {
    routerMock = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService,
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    // @ts-ignore
    component['router'] = routerMock as Router;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // ---------------------------
  // VALIDATION DES CHAMPS FORM
  // ---------------------------

  describe('Validation du formulaire', () => {

    describe('Champ email', () => {
      it('devrait être invalide si vide', () => {
        const email = component.form.get('email');
        email?.setValue('');
        expect(email?.hasError('required')).toBe(true);
      });

      it('devrait être invalide si format incorrect', () => {
        const email = component.form.get('email');
        email?.setValue('not-an-email');
        expect(email?.hasError('email')).toBe(true);
      });

      it('devrait être valide avec un email correct', () => {
        const email = component.form.get('email');
        email?.setValue('test@test.com');
        expect(email?.valid).toBe(true);
      });
    });

    describe('Champ firstName', () => {
      it('devrait être invalide si vide', () => {
        const firstName = component.form.get('firstName');
        firstName?.setValue('');
        expect(firstName?.hasError('required')).toBe(true);
      });

      it('devrait être invalide si trop court (<3)', () => {
        const firstName = component.form.get('firstName');
        firstName?.setValue('ab');
        expect(firstName?.hasError('minlength')).toBe(true);
      });

      it('devrait être invalide si trop long (>20)', () => {
        const firstName = component.form.get('firstName');
        firstName?.setValue('a'.repeat(21));
        expect(firstName?.hasError('maxlength')).toBe(true);
      });

      it('devrait être valide avec une taille correcte', () => {
        const firstName = component.form.get('firstName');
        firstName?.setValue('Jean');
        expect(firstName?.valid).toBe(true);
      });
    });

    describe('Champ lastName', () => {
      it('devrait être invalide si vide', () => {
        const lastName = component.form.get('lastName');
        lastName?.setValue('');
        expect(lastName?.hasError('required')).toBe(true);
      });

      it('devrait être invalide si trop court (<3)', () => {
        const lastName = component.form.get('lastName');
        lastName?.setValue('ab');
        expect(lastName?.hasError('minlength')).toBe(true);
      });

      it('devrait être invalide si trop long (>20)', () => {
        const lastName = component.form.get('lastName');
        lastName?.setValue('a'.repeat(21));
        expect(lastName?.hasError('maxlength')).toBe(true);
      });

      it('devrait être valide avec une taille correcte', () => {
        const lastName = component.form.get('lastName');
        lastName?.setValue('Dupont');
        expect(lastName?.valid).toBe(true);
      });
    });

    describe('Champ password', () => {
      it('devrait être invalide si vide', () => {
        const password = component.form.get('password');
        password?.setValue('');
        expect(password?.hasError('required')).toBe(true);
      });

      it('devrait être invalide si trop court (<3)', () => {
        const password = component.form.get('password');
        password?.setValue('ab');
        expect(password?.hasError('minlength')).toBe(true);
      });

      it('devrait être invalide si trop long (>40)', () => {
        const password = component.form.get('password');
        password?.setValue('a'.repeat(41));
        expect(password?.hasError('maxlength')).toBe(true);
      });

      it('devrait être valide avec une taille correcte', () => {
        const password = component.form.get('password');
        password?.setValue('password123');
        expect(password?.valid).toBe(true);
      });
    });
  });

  // ----------------------------------------
  // TESTS DE LA MÉTHODE submit() DU FORMULAIRE
  // ----------------------------------------

  describe('submit()', () => {

    beforeEach(() => {
      // Reset du flag erreur avant chaque submit
      component.onError = false;
    });

    it('devrait appeler authService.register et naviguer sur succès', () => {
      const spyRegister = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
      const spyNavigate = jest.spyOn(routerMock, 'navigate');

      component.form.setValue({
        email: 'test@test.com',
        firstName: 'Jean',
        lastName: 'Dupont',
        password: 'password123'
      });

      component.submit();

      expect(spyRegister).toHaveBeenCalledWith({
        email: 'test@test.com',
        firstName: 'Jean',
        lastName: 'Dupont',
        password: 'password123'
      });

      expect(spyNavigate).toHaveBeenCalledWith(['/login']);
      expect(component.onError).toBe(false);
    });

    it('devrait définir onError à true si register échoue', () => {
      jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Failed')));
      component.form.setValue({
        email: 'test@test.com',
        firstName: 'Jean',
        lastName: 'Dupont',
        password: 'password123'
      });

      component.submit();

      expect(component.onError).toBe(true);
      expect(routerMock.navigate).not.toHaveBeenCalled();
    });

    
  });
});
