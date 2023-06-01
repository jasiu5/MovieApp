import { fireEvent, render, screen } from '@testing-library/react';
import App from './App';
import MoviesManagement from './pages/moviesManagement';

const fetchMock = (status, data) => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      status: status,
      json: () => Promise.resolve(data),
    })
  );
}

test('renders home page', () => {
  render(<App />);

  const navButton = screen.getByRole('button', {
    name: /Logowanie/i
  });

  expect(navButton).toBeEnabled();
  fireEvent.click(navButton);
});

test('MoviesManagement: movies list', () => {
  fetchMock(200, [{
    title: 'title 1',
    description: 'desc 1',
    id: 1
  }, {
    title: 'title 2',
    description: 'desc 2',
    id: 2
  }]);

  render(<MoviesManagement />);

  const info = screen.queryAllByText(/Nie dod/i);
  expect(info).toHaveLength(0);
});