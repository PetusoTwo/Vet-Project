package gui;

import java.awt.Color;
import java.awt.print.PrinterException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import DB.ConexionOracle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import javax.swing.ImageIcon;

public class Boleta extends javax.swing.JFrame {
    private String idCliente;
    private String idMascot;
    // Datos del dueño
    private String nombreDueño;
    private String telefono;
    // Datos de la mascota
    private String nombreMascota;
    private String especie;
    // Tratamientos
    private String tPrimario;
    private String tSegundario;
    // id y precios
    private String tratamiento_id_principal;
    private Double precio_principal;
    private String tratamientos_id_secundario;
    private Double precio_segundario;

    public Boleta(String idCliente, String idMascot, String tPrimario, String tSegundario) {
        initComponents();
        this.setTitle("Vet Link - Boleta");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/logocircle.png"));
        Image logo = icon.getImage();
        setIconImage(logo);
        this.idCliente = idCliente;
        this.idMascot = idMascot;
        this.tPrimario = tPrimario;
        this.tSegundario = tSegundario;
        cargarDatos(idCliente, idMascot);
        cargarTratamientos();
        mostrarBoleta();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMainBoleta = new javax.swing.JPanel();
        btnAccept = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        txtBoleta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMainBoleta.setBackground(new java.awt.Color(86, 168, 124));
        panelMainBoleta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAccept.setBackground(new java.awt.Color(13, 92, 141));
        btnAccept.setFont(new java.awt.Font("Leelawadee UI", 0, 18)); // NOI18N
        btnAccept.setForeground(new java.awt.Color(255, 255, 255));
        btnAccept.setText("Aceptar");
        btnAccept.setToolTipText("");
        btnAccept.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.white));
        btnAccept.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAccept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAcceptMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAcceptMouseExited(evt);
            }
        });
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });
        panelMainBoleta.add(btnAccept, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 730, 180, 40));

        btnPrint.setBackground(new java.awt.Color(121, 180, 211));
        btnPrint.setFont(new java.awt.Font("Leelawadee UI", 0, 18)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(0, 51, 153));
        btnPrint.setText("Imprimir");
        btnPrint.setToolTipText("");
        btnPrint.setBorder(null);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPrintMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPrintMouseExited(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        panelMainBoleta.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 730, 170, 40));

        txtBoleta.setBackground(new java.awt.Color(255, 255, 255));
        txtBoleta.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtBoleta.setForeground(new java.awt.Color(0, 102, 153));
        txtBoleta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        panelMainBoleta.add(txtBoleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 440, 670));

        getContentPane().add(panelMainBoleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcceptMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnAcceptMouseEntered
        btnAccept.setBackground(new Color(121, 180, 211));
    }// GEN-LAST:event_btnAcceptMouseEntered

    private void btnAcceptMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnAcceptMouseExited
        btnAccept.setBackground(new Color(13, 92, 141));
    }// GEN-LAST:event_btnAcceptMouseExited

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAcceptActionPerformed
        String boleta_id = generarID();
        Double totalSinIgv = precio_principal + precio_segundario;
        Double igv = totalSinIgv * 0.18;
        Double total = totalSinIgv + igv;
        try (Connection conn = ConexionOracle.getConnection()) {
            String sqlBoleta = "INSERT INTO BOLETA (boleta_id, cliente_id, mascota_id, tratamiento_id_principal, tratamiento_id_secundario, total_sin_igv, igv, total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psBoleta = conn.prepareStatement(sqlBoleta);
            psBoleta.setString(1, boleta_id);
            psBoleta.setString(2, idCliente);
            psBoleta.setString(3, idMascot);
            psBoleta.setString(4, tratamiento_id_principal);
            psBoleta.setString(5, tratamientos_id_secundario);
            psBoleta.setDouble(6, totalSinIgv);
            psBoleta.setDouble(7, igv);
            psBoleta.setDouble(8, total);
            psBoleta.setString(9, "PAGADO");
            psBoleta.executeUpdate();
            JOptionPane.showMessageDialog(null, "Boleta registrada con éxito", "Felicidades",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la boleta", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }// GEN-LAST:event_btnAcceptActionPerformed

    private void btnPrintMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnPrintMouseEntered
        btnPrint.setBackground(new Color(202, 210, 210));
    }// GEN-LAST:event_btnPrintMouseEntered

    private void btnPrintMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnPrintMouseExited
        btnPrint.setBackground(new Color(121, 180, 211));
    }// GEN-LAST:event_btnPrintMouseExited

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrintActionPerformed
        // Metodo para imprimir
        try {
            // Obtiene un objeto PrinterJob para gestionar la impresión
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    g2d.scale(0.8, 0.8);
                    txtBoleta.printAll(graphics);
                    return PAGE_EXISTS;
                }
            });
            boolean printAccepted = printerJob.printDialog();
            if (printAccepted) {
                printerJob.print();
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }// GEN-LAST:event_btnPrintActionPerformed

    // Método privado para generar el ID
    private static String generarID() {
        int ID_LENGTH = 8;
        SecureRandom random = new SecureRandom();
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    private void cargarDatos(String idCliente, String idMascot) {
        String sqlCargarMascota = "SELECT nombre, especie FROM MASCOTA WHERE mascota_id = ?";
        String sqlCargarDueño = "SELECT nombre, telefono FROM CLIENTE WHERE cliente_id = ?";
        try (Connection conn = ConexionOracle.getConnection()) {
            // Cargar datos de la mascota
            PreparedStatement psMascota = conn.prepareStatement(sqlCargarMascota);
            psMascota.setString(1, idMascot);
            ResultSet rsMascot = psMascota.executeQuery();
            if (rsMascot.next()) {
                this.nombreMascota = rsMascot.getString("nombre");
                this.especie = rsMascot.getString("especie");
            }
            // Cargar datos del dueño
            PreparedStatement psDueño = conn.prepareStatement(sqlCargarDueño);
            psDueño.setString(1, idCliente);
            ResultSet rsDueño = psDueño.executeQuery();
            if (rsDueño.next()) {
                this.nombreDueño = rsDueño.getString("nombre");
                this.telefono = rsDueño.getString("telefono");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del cliente o la mascota", "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Cargar precios
    private void cargarTratamientos() {
        String sqlTratamiento = "SELECT tratamiento_id, precio FROM TRATAMIENTO WHERE nombre_tratamiento = ?";
        try (Connection conn = ConexionOracle.getConnection()) {
            // Tratamiento primario
            PreparedStatement psPrimario = conn.prepareStatement(sqlTratamiento);
            psPrimario.setString(1, tPrimario);
            ResultSet rsPrimario = psPrimario.executeQuery();
            if (rsPrimario.next()) {
                this.tratamiento_id_principal = rsPrimario.getString("tratamiento_id");
                this.precio_principal = rsPrimario.getDouble("precio");
            }
            // Tratamiento secundario
            PreparedStatement psSecundario = conn.prepareStatement(sqlTratamiento);
            psSecundario.setString(1, tSegundario);
            ResultSet rsSecundario = psSecundario.executeQuery();
            if (rsSecundario.next()) {
                this.tratamientos_id_secundario = rsSecundario.getString("tratamiento_id");
                this.precio_segundario = rsSecundario.getDouble("precio");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los tratamientos", "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarBoleta() {
        Double totalSinIgv = precio_principal + precio_segundario;
        Double igv = totalSinIgv * 0.18;
        Double total = totalSinIgv + igv;

        String boletaTexto = "<html>" +
"<table width='440' height='670' cellpadding='10' style='border-collapse: collapse; font-family: Arial, sans-serif; font-size: 12px;'>"+
    "<tr>" +
        "<td colspan='2' style='background-color: rgb(13, 92, 141); color: white; text-align: center; font-size: 18px;'><strong>Boleta de Venta</strong></td>" +
    "</tr>" +
    "<tr>" +
        "<td style='width: 50%;'><strong style='color: rgb(13, 92, 141);'>Cliente:</strong> " + nombreDueño + "<br>" +
        "<strong style='color: rgb(13, 92, 141);'>Teléfono:</strong> " + telefono + "</td>" +
        "<td style='width: 50%;'><strong style='color: rgb(13, 92, 141);'>Mascota:</strong> " + nombreMascota + "<br>" +
        "<strong style='color: rgb(13, 92, 141);'>Especie:</strong> " + especie + "</td>" +
    "</tr>" +
    "<tr>" +
        "<td colspan='2'>" +
        "<table width='100%' style='font-size: 14px; border-collapse: collapse;'>"+
            "<tr><td><strong style='color: rgb(13, 92, 141);'>Tratamiento Primario:</strong></td><td style='color: rgb(151, 189, 183); font-weight: bold;'>" + tPrimario + "</td><td>(S/ " + precio_principal + ")</td></tr>" +
            "<tr><td><strong style='color: rgb(13, 92, 141);'>Tratamiento Secundario:</strong></td><td style='color: rgb(151, 189, 183); font-weight: bold;'>" + tSegundario + "</td><td>(S/ " + precio_segundario + ")</td></tr>" +
        "</table>" +
        "</td>" +
    "</tr>" +
    "<tr>" +
        "<td colspan='2'>" +
        "<p style='font-size: 14px;'>" +
        "<strong style='color: rgb(13, 92, 141);'>Total sin IGV:</strong> S/ " + totalSinIgv + "<br>" +
        "<strong style='color: rgb(13, 92, 141);'>IGV (18%):</strong> S/ " + igv + "<br>" +
        "<strong style='color: rgb(13, 92, 141); font-size: 16px;'>Total:</strong> S/ " + total +
        "</p>" +
        "</td>" +
    "</tr>" +
"</table>" +
"</html>";

        txtBoleta.setText(boletaTexto);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnPrint;
    private javax.swing.JPanel panelMainBoleta;
    private javax.swing.JLabel txtBoleta;
    // End of variables declaration//GEN-END:variables
}
