import { render } from '@testing-library/angular';
import { Navbar } from './navbar';

describe('Navbar', () => {
  it('should render the navbar brand', async () => {
    const { getByText } = await render(Navbar);
    expect(getByText('Tontine Manager')).toBeTruthy();
  });
});
