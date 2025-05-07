import unittest
from unittest.mock import patch
from user_code import jugar_piedra_papel_tijeras

class TestPiedraPapelTijeras(unittest.TestCase):

    @patch('random.choice', return_value="tijeras")  # Simulamos que la computadora elige tijeras
    @patch('builtins.input', return_value="piedra")  # Simulamos que el usuario elige piedra
    def test_usuario_gana(self, mock_input, mock_random):
        computadora, resultado = jugar_piedra_papel_tijeras()
        self.assertEqual(computadora, "tijeras")  # Verificamos que la computadora eligió tijeras
        self.assertEqual(resultado, "¡Ganaste!")  # Verificamos que el resultado es el esperado

    @patch('random.choice', return_value="papel")  # Simulamos que la computadora elige papel
    @patch('builtins.input', return_value="piedra")  # Simulamos que el usuario elige piedra
    def test_computadora_gana(self, mock_input, mock_random):
        computadora, resultado = jugar_piedra_papel_tijeras()
        self.assertEqual(computadora, "papel")  # Verificamos que la computadora eligió papel
        self.assertEqual(resultado, "Perdiste")  # Verificamos que el resultado es el esperado

    @patch('random.choice', return_value="piedra")  # Simulamos que la computadora elige piedra
    @patch('builtins.input', return_value="piedra")  # Simulamos que el usuario elige piedra
    def test_empate(self, mock_input, mock_random):
        computadora, resultado = jugar_piedra_papel_tijeras()
        self.assertEqual(computadora, "piedra")  # Verificamos que la computadora eligió piedra
        self.assertEqual(resultado, "Empate")  # Verificamos que el resultado es un empate

if __name__ == '__main__':
    unittest.main()
