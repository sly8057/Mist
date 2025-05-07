import unittest
from unittest.mock import patch
from user_code import generar_color

class TestGenerarColor(unittest.TestCase):

    @patch('random.randint', side_effect=[255, 0, 128])  # Simulamos que los valores de R, G y B son 255, 0 y 128
    def test_generar_color(self, mock_randint):
        resultado = generar_color()
        self.assertEqual(resultado, "RGB(255, 0, 128)")  # Verificamos que el formato es el esperado

    @patch('random.randint', side_effect=[0, 0, 0])  # Todos los valores son 0
    def test_generar_color_negro(self, mock_randint):
        resultado = generar_color()
        self.assertEqual(resultado, "RGB(0, 0, 0)")  # Verificamos que el color negro sea generado correctamente

    @patch('random.randint', side_effect=[255, 255, 255])  # Todos los valores son 255
    def test_generar_color_blanco(self, mock_randint):
        resultado = generar_color()
        self.assertEqual(resultado, "RGB(255, 255, 255)")  # Verificamos que el color blanco sea generado correctamente

if __name__ == '__main__':
    unittest.main()