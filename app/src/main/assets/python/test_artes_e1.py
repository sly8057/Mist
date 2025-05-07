import unittest
from io import StringIO
from contextlib import redirect_stdout
from user_code import imprimir_figura # Asegúrate de que el archivo se llame figuras.py

class TestImprimirFigura(unittest.TestCase):
    def capturar_salida(self, figura, tamano):
        salida = StringIO()
        with redirect_stdout(salida):
            imprimir_figura(figura, tamano)
        return salida.getvalue()

    def test_cuadro_tamano_3(self):
        esperado = "# # # \n# # # \n# # # \n"
        resultado = self.capturar_salida("cuadro", 3)
        self.assertEqual(resultado, esperado)

    def test_triangulo_tamano_4(self):
        esperado = "* \n* * \n* * * \n* * * * \n"
        resultado = self.capturar_salida("triangulo", 4)
        self.assertEqual(resultado, esperado)

    def test_figura_desconocida(self):
        resultado = self.capturar_salida("circulo", 4)
        self.assertEqual(resultado, "")  # No imprime nada

    if __name__ == '__main__':
        unittest.main()