import { render } from '@testing-library/angular';
import { NavbarComponent } from './navbar';

describe('NavbarComponent', () => {
  it('should render the navbar brand', async () => {
    const { getByText } = await render(NavbarComponent);
    expect(getByText('Tontine Manager')).toBeTruthy();
  });
});
