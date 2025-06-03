import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  
  it('should call register and navigate on success', () => {
    const spy = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    const navSpy = jest.spyOn(routerMock, 'navigate');
    component.form.setValue({
      email: 'test@test.com',
      firstName: 'first',
      lastName: 'last',
      password: 'pass'
    });
    component.submit();
    expect(spy).toHaveBeenCalled();
    expect(navSpy).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on register error', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => 'Error'));
    component.form.setValue({
      email: 'test@test.com',
      firstName: 'first',
      lastName: 'last',
      password: 'pass'
    });
    component.submit();
    expect(component.onError).toBe(true);
  });

  it('should have invalid form with missing fields', () => {
    // All fields missing
    component.form.setValue({ email: '', firstName: '', lastName: '', password: '' });
    expect(component.form.valid).toBe(false);

    // Email missing
    component.form.setValue({ email: '', firstName: 'far', lastName: 'lar', password: 'par' });
    expect(component.form.valid).toBe(false);

    // Password missing
    component.form.setValue({ email: 'e@e.com', firstName: 'far', lastName: 'lar', password: '' });
    expect(component.form.valid).toBe(false);

    // First name missing
    component.form.setValue({ email: 'e@e.com', firstName: '', lastName: 'lar', password: 'par' });
    expect(component.form.valid).toBe(false);

    // Last name missing
    component.form.setValue({ email: 'e@e.com', firstName: 'far', lastName: '', password: 'par' });
    expect(component.form.valid).toBe(false);
  });
});