import unittest
from unittest.mock import patch
from user_code import paleta_colores

class TestPaletaColores(unittest.TestCase):

    @patch('random.sample', return_value=["Rojo", "Azul", "Verde"])
    def test_paleta_colores_3(self, mock_sample):
        resultado = paleta_colores(3)
        self.assertEqual(resultado, ["Rojo", "Azul", "Verde"])  # Verificamos que la paleta sea correcta

    @patch('random.sample', return_value=["Rojo", "Verde", "Morado", "Naranja"])
    def test_paleta_colores_4(self, mock_sample):
        resultado = paleta_colores(4)
        self.assertEqual(resultado, ["Rojo", "Verde", "Morado", "Naranja"])

    @patch('random.sample', return_value=["Azul", "Amarillo"])
    def test_paleta_colores_2(self, mock_sample):
        resultado = paleta_colores(2)
        self.assertEqual(resultado, ["Azul", "Amarillo"])

    @patch('random.sample', return_value=["Rojo", "Verde", "Azul", "Morado", "Amarillo"])
    def test_paleta_colores_5(self, mock_sample):
        resultado = paleta_colores(5)
        self.assertEqual(resultado, ["Rojo", "Verde", "Azul", "Morado", "Amarillo"])

if __name__ == '__main__':
    unittest.main()