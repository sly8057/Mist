import unittest
from unittest.mock import patch
from user_code import generar_historia

class TestGeneradorHistoria(unittest.TestCase):

    @patch('random.choice')
    def test_historia_controlada(self, mock_choice):
        mock_choice.side_effect = ["una princesa", "en el bosque", "encontró un tesoro"]
        resultado = generar_historia()
        self.assertEqual(resultado, "una princesa en el bosque encontró un tesoro.")

    def test_historia_formato(self):
        resultado = generar_historia()
        # self.assertTrue(resultado.endswith("."))
        self.assertIsInstance(resultado, str)
        self.assertGreater(len(resultado), 10)

if __name__ == "__main__":
    unittest.main()