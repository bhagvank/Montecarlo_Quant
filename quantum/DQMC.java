
import java.lang.*;
import java.io.*;
import java.util.*;

public class DQMC {

  public DQMC()
  {

  }

  final static int lx = 13, ly = 15;
  final static int L = lx*ly, N = L, nv = 3*N, nc = 200, ne = 500, bmax = 100;
  final static double hm = 12.119232, rm = 2.9683, e0 = 10.956, u0 = 206875.0;
  final static double g0 = 11.027235, a3 = 6.386763, a4 = 12.116816;
  final static double a0 = 10.5717543, b0 =  2.07758779, v0 = 186924.404;
  final static double c6 = 1.35186623, c8 = 0.41495143, c10 = 0.17151143;
  final static double d = 1.438, rho = 0.024494, rho2D = 0.0637;
  static int seed, iq, is, ic, ig, ia, ng, nf, ns, nr, np, dn, step;
  static double ax, dx, dy, dz, dr, norm, hideal;
  static double e, er, ek, ep, w, ws, ka, dt, st, ze, z0, r0, a, b, c;
  static double phi[] = new double[N];
  static double x[] = new double[N];
  static double y[] = new double[N];
  static double z[] = new double[N];
  static double za[] = new double[N];
  static double zb[] = new double[N];
  static double xx[][] = new double[N][N];
  static double yy[][] = new double[N][N];
  static double zz[][] = new double[N][N];
  static double rr[][] = new double[N][N];
  static double sx[] = new double[L];
  static double sy[] = new double[L];
  static double sz[] = new double[L];
  static double xs[][] = new double[N][L];
  static double ys[][] = new double[N][L];
  static double zs[][] = new double[N][L];
  static double rs[][] = new double[N][L];
  static double conf[] = new double[nv];
  static double ensm[][] = new double[nv][ne];
  static double vix[] = new double[N];
  static double viy[] = new double[N];
  static double viz[] = new double[N];
  static double vd[] = new double[nv];
  static double eold[] = new double[ne];
  static double ecrl[] = new double[nc];
  static double gr[] = new double[bmax];
  static int hist[] = new int[bmax];

  public static void main(String argv[]) throws IOException {

    DQMC dq = new DQMC();
    dq.initiate();

    if (ic != 0)
      dq.cqmc();
    else
      switch (iq) {
        case (1): dq.vqmc();
          break;
        case (2): dq.dqmc();
      }

    if (is != 0) dq.save();


    if ( ig != 0){
      PrintWriter q = new PrintWriter
        (new FileOutputStream("gr.dat"), true);
      for (int i=0; i<bmax; ++i) {
        double r = (i+0.5)*dr;
        q.println(r + "  " + gr[i]);
      }
    }
  }

  public void initiate() throws IOException {

    GregorianCalendar t = new GregorianCalendar();
    int t1 = t.get(Calendar.SECOND);
    int t2 = t.get(Calendar.MINUTE);
    int t3 = t.get(Calendar.HOUR_OF_DAY);
    int t4 = t.get(Calendar.DAY_OF_MONTH);
    int t5 = t.get(Calendar.MONTH)+1;
    int t6 = t.get(Calendar.YEAR);
    seed = t6+70*(t5+12*(t4+31*(t3+23*(t2+59*t1))));
    if ((seed%2) == 0) seed = seed-1;

    try {
      BufferedReader r = new BufferedReader
        (new InputStreamReader(new FileInputStream("fort.15")));
      StringTokenizer s = new StringTokenizer(r.readLine());
      iq = Integer.parseInt(s.nextToken());
      is = Integer.parseInt(s.nextToken());
      ic = Integer.parseInt(s.nextToken());
      ig = Integer.parseInt(s.nextToken());
      dn = Integer.parseInt(s.nextToken());
      np = ne - dn;

      s = new StringTokenizer(r.readLine());
      ng = Integer.parseInt(s.nextToken());
      nf = Integer.parseInt(s.nextToken());
      ns = Integer.parseInt(s.nextToken());
      nr = Integer.parseInt(s.nextToken());
      dr = Double.parseDouble(s.nextToken());

      s = new StringTokenizer(r.readLine());
      ax = Double.parseDouble(s.nextToken());
      dx = Double.parseDouble(s.nextToken());
      dt = Double.parseDouble(s.nextToken());
      ka = Double.parseDouble(s.nextToken());
      ze = Double.parseDouble(s.nextToken());

      s = new StringTokenizer(r.readLine());
      z0 = Double.parseDouble(s.nextToken());
      r0 = Double.parseDouble(s.nextToken());
      a  = Double.parseDouble(s.nextToken());
      b  = Double.parseDouble(s.nextToken());
      c  = Double.parseDouble(s.nextToken());
    }
    catch (Exception e) {
      System.out.println("Exception error: " + e.toString());
    }

    System.out.println("Quantum Monte Carlo Simulation of "
      + N + " Helium-4 Atoms on a Graphite Surface");
    if (iq == 1)
      System.out.println("Variational Simulation with Parameters:");
    if (iq == 2)
      System.out.println("Diffusion Simulation with Parameters:");
    System.out.println("I_QMC = " + iq);
    System.out.println("I_SAVE = " + is);
    System.out.println("I_CORRELATION = " + ic);
    System.out.println("I_PAIR = " + ig);
    System.out.println("N_ENSEMBLE = " + np);
    System.out.println("N_GROUP = " + ng);
    System.out.println("N_FREQUENCE = " + nf);
    System.out.println("N_SIZE = " + ns);
    System.out.println("N_EQUILIBRIUM = " + nr);
    System.out.println("AX = " + ax);
    System.out.println("DX = " + dx);
    System.out.println("DT = " + dt);
    System.out.println("DR = " + dr);
    System.out.println("kappa = " + ka);
    System.out.println("zeta = " + ze);
    System.out.println("z0 = " + z0);
    System.out.println("r0 = " + r0);
    System.out.println("a  = " + a);
    System.out.println("b  = " + b);
    System.out.println("c  = " + c);
    System.out.println("The random number initial seed is: " + seed);

    st = Math.sqrt(hm*dt);
    dy = dx;
    dz = dx;
    r0 *= r0;
    z0 *= z0;
    a *= a;
    b *= b;
    c *= c;
    if (ig == 3)
      norm = 4*Math.PI*rho/3;
    else if (ig == 2)
      norm = Math.PI*rho2D;
    step = 0;

 // Set up the virtual lattice with lx*ly points centered at (0,0,ze)

    double ay = Math.sqrt(3.0)*ax/2;
    double axh = ax/2;
    double xh = ((lx-1)*ax+axh)/2;
    double yh= (ly-1)*ay/2;
    int ik = 0;
    for (int j=0; j<ly; j++) {
      int ishift = j%2;
      for (int i=0; i<lx; i++) {
        sx[ik] = i*ax+ishift*axh-xh;
        sy[ik] = j*ay-yh; 
        sz[ik] = ze;
        ik++;
      }
    }

    if (is == 2) {
      try {
        DataInputStream r = new DataInputStream
          (new FileInputStream("data.in"));
        np = r.readInt();
        seed = r.readInt();
        step = r.readInt();
        er = r.readDouble();
        for (int i=0; i<np; ++i){
          eold[i] = r.readDouble();
          for (int j=0; j<nv; ++j)
            ensm[j][i] = r.readDouble();
        }
      }
      catch (Exception e) {
        System.out.println("Exception error: " + e.toString());
      }
    }
  }


  public void cqmc() {


    if (is != 2) {
      switch (iq) {
        case (1): configure();
        break;
        case (2): ensemble();
      }
    }


    for (int i=0; i<nr; ++i) {
      switch (iq) {
        case (1): metropolis();
        break;
        case (2): move();
      }
    }


    for (int i=0; i<nc; ++i) {
      if (iq == 1) {
       metropolis();
       energy();
      }
      else
        move();
      ecrl[i] = e;
    }
    auto();


    if (is != 0) save();

  }


  public void vqmc() {


    if (is != 2) configure();


    for (int i=0; i<nr; ++i) metropolis();


    double sum0 = 0;
    double sum1 = 0;
    double kum1 = 0;
    double sum2 = 0;
    double kum2 = 0;
    double sum3 = 0;
    double kum3 = 0;
    for (int i=0; i<ng; ++i) {
      double ge = 0;
      double gk = 0;
      ia = 0;
      for (int j=0; j<nf*ns; ++j) {
        metropolis();
        if (j%nf == 0) {
          energy();
          ge += e;
          gk += ek;
          sum2 += e*e;
          kum2 += ek*ek;
          if (ig != 0) pair();
        }
      }
      sum0 += ia;
      sum1 += ge;
      kum1 += gk;
      sum3 += (ge/ns)*(ge/ns);
      kum3 += (gk/ns)*(gk/ns);
    }
    int nt = ng*ns;
    double ev = sum1/nt;
    double kv = kum1/nt;
    double sig1 = Math.sqrt((sum2/nt-ev*ev)/nt);
    double kig1 = Math.sqrt((kum2/nt-kv*kv)/nt);
    double sig2 = Math.sqrt((sum3/ng-ev*ev)/ng);
    double kig2 = Math.sqrt((kum3/ng-kv*kv)/ng);
    sum0 /= nt*nf;
    int i0 = (int)(sum0*100);


    if (ig != 0) {
      for (int i=0; i<bmax; ++i) {
        double rl = i*dr;
        double ru = rl+dr;
        if (ig == 3)
          hideal = norm*(ru*ru*ru-rl*rl*rl);
        else if (ig == 2)
          hideal = norm*(ru*ru-rl*rl);
        gr[i] = (double)hist[i]/(N*nt*hideal);
      }
    }

    System.out.println("Energy/atom =  " + ev/N + " +- " + sig1/N + " K");
    System.out.println("Variance from groups = " + sig2/N + " K");
    System.out.println("K_Energy/atom =  " + kv/N + " +- " + kig1/N + " K");
    System.out.println("Variance from groups = " + kig2/N + " K");
    System.out.println("Acceptance rate = " + i0 + "%");
  }


  public void dqmc() {


    if (is != 2) ensemble();


    ws = np;
    for (int i=0; i<nr; ++i) move();


    double sum1 = 0;
    double kum1 = 0;
    double sum2 = 0;
    double kum2 = 0;
    double sum3 = 0;
    double kum3 = 0;
    int nsum = 0;
    for (int i=0; i<ng; ++i) {
      double ge = 0;
      double gk = 0;
      for (int j=0; j<nf*ns; ++j) {
        move();
        if (j%nf == 0) {
          ge += e;
          gk += ek;
          sum2 += e*e;
          kum2 += ek*ek;
          if (ig != 0) {
            for (int k=0; k<np; ++k) {
              for (int l=0; l<nv; ++l) conf[l] = ensm[l][k];
              pair();
            }
            nsum += np;
          }
        }
      }
      sum1 += ge;
      kum1 += gk;
      sum3 += (ge/ns)*(ge/ns);
      kum3 += (gk/ns)*(gk/ns);
    }
    int nt = ng*ns;
    double ev = sum1/nt;
    double kv = kum1/nt;
    double sig1 = Math.sqrt((sum2/nt-ev*ev)/nt);
    double kig1 = Math.sqrt((kum2/nt-kv*kv)/nt);
    double sig2 = Math.sqrt((sum3/ng-ev*ev)/ng);
    double kig2 = Math.sqrt((kum3/ng-kv*kv)/ng);


    if (ig != 0) {
      for (int i=0; i<bmax; ++i) {
        double rl = i*dr;
        double ru = rl+dr;
        if (ig == 3)
          hideal = norm*(ru*ru*ru-rl*rl*rl);
        else if (ig == 2)
          hideal = norm*(ru*ru-rl*rl);
        gr[i] = (double)hist[i]/(N*nsum*hideal);
      }
    }

    System.out.println("Energy/atom =  " + ev/N + " +- " + sig1/N + " K");
    System.out.println("Variance from groups = " + sig2/N + " K");
    System.out.println("K_Energy/atom =  " + kv/N + " +- " + kig1/N + " K");
    System.out.println("Variance from groups = " + kig2/N + " K");
  }


  public static void configure() {

    int k = 0;
    while (k<N) {
      x[k] = sx[k] + (ranf()-0.5)*dx;
      y[k] = sy[k] + (ranf()-0.5)*dy;
      z[k] = sz[k] + (ranf()-0.5)*dz;
      ++k;
    }

    for (int i=0; i<N; ++i) {
      conf[i] = x[i];
      conf[N+i] = y[i];
      conf[2*N+i] = z[i];
    }

    wave();
  }


  public static void wave() {


    for (int i=0; i<N; ++i) {
      x[i] = conf[i];
      y[i] = conf[N+i];
      z[i] = conf[2*N+i];
    }


    double psi = 1.0;
    for (int i=0; i<N; ++i) {
      phi[i] = 0;
      for (int j=0; j<L; ++j) {
        xs[i][j] = x[i]-sx[j];
        ys[i][j] = y[i]-sy[j];
        zs[i][j] = z[i]-sz[j];
        rs[i][j] = (xs[i][j]*xs[i][j]+ys[i][j]*ys[i][j]
                  +zs[i][j]*zs[i][j])/r0;
        phi[i] += xe(rs[i][j]); 
      }
    }
    for (int i=0; i<N; ++i) {
      zb[i] = z[i] - ze;
      za[i] = zb[i]*zb[i]/z0;
      phi[i] *= xe(za[i]);
      psi *= phi[i];
    }
    double chi = -Math.log(psi);


    double ut = 0;
    for (int i=0; i<N-1; ++i) {
      for (int j=i+1; j<N; ++j) {
        xx[i][j] = x[i]-x[j];
        yy[i][j] = y[i]-y[j];
        zz[i][j] = z[i]-z[j];
        rr[i][j] = xx[i][j]*xx[i][j]
                  +yy[i][j]*yy[i][j]+zz[i][j]*zz[i][j];
        ut += u(rr[i][j]);
      }
    }


    w = -2*(chi+ut);
  }


  public static void energy() {
  double ei[] = new double[N];
  double ez[] = new double[N];

    drift();


    double ed = 0;
    for (int i=0; i<nv; ++i) ed += vd[i]*vd[i];
    ed /= 2*hm;


    ep  = 0;
    double ek2 = 0;
    for (int i=0; i<N-1; ++i) {
      for (int j=i+1; j<N; ++j) {
        ek2 += up(rr[i][j]);
        double xd = Math.sqrt(rr[i][j])/rm;
        ep += v(xd);
      }
    }


    for (int i=0; i<N; ++i) ep += ue(z[i]/rm);


    for (int i=0; i<N; ++i) {
      for (int j=0; j<L; ++j) {
        ei[i] += rs[i][j]*xe(rs[i][j]);
        ez[i] += zs[i][j]*xe(rs[i][j]);
      }
    }
    double ek11 = 0;
    double ek12 = 0;
    for (int i=0; i<N; ++i) {
      ek11 += za[i];
      ek12 += xe(za[i])*(ei[i]+2*zb[i]*ez[i]/z0)/phi[i];
    }
    double ek1 = (6*N-4*ek12)/r0+(2*N-4*ek11)/z0;

    for (int i=0; i<N; ++i)
      ek1 += vix[i]*vix[i]+viy[i]*viy[i]+viz[i]*viz[i];

    double et = hm*(ek1/4+ek2/2);
    ek = 2*et-ed;
    ep *= e0;
    e = ek+ep;
  }



  public static void drift() {
    double vx[] = new double[N];
    double vy[] = new double[N];
    double vz[] = new double[N];

    wave();


    for (int i=0; i<N; ++i){
      vix[i] = 0;
      viy[i] = 0;
      viz[i] = 0;
    }


    for (int i=0; i<N-1; ++i){
      for (int j=i+1; j<N; ++j){
        double gr = u1(rr[i][j]);
        double gx = gr*xx[i][j];
        double gy = gr*yy[i][j];
        double gz = gr*zz[i][j];
        vx[i] -= gx;
        vy[i] -= gy;
        vz[i] -= gz;
        vx[j] += gx;
        vy[j] += gy;
        vz[j] += gz;
      }
    }


    for (int i=0; i<N; ++i){
      for (int j=0; j<L; ++j){
         vix[i] -= xs[i][j]*xe(rs[i][j]);
         viy[i] -= ys[i][j]*xe(rs[i][j]);
         viz[i] -= zs[i][j]*xe(rs[i][j]);
      }
    }
    for (int i=0; i<N; ++i){
      vix[i] *= 2*xe(za[i])/phi[i]/r0;
      viy[i] *= 2*xe(za[i])/phi[i]/r0;
      viz[i] *= 2*xe(za[i])/phi[i]/r0;
      viz[i] -= 2*zb[i]/z0;
    }


    for (int i=0; i<N; ++i){
      vd[i] = hm*(vx[i]+vix[i]);
      vd[N+i] = hm*(vy[i]+viy[i]);
      vd[2*N+i] = hm*(vz[i]+viz[i]);
    }
  }


  public static void ensemble() {
    configure();


    for (int i=0; i<nr; ++i) metropolis();


    er = 0;
    for (int i=0; i<nf*np; ++i) {
      metropolis();
      if (i%nf == 0) {
        int j = i/nf;
        energy();
        eold[j] = e;
        er += e;
        for (int k=0; k<nv; ++k) ensm[k][j] = conf[k];
      }
    }
    er /= np;
  }


  public static void metropolis() {

    double cfsv[] = new double[nv];


    for (int i=0; i<N; ++i) {
      cfsv[i] = conf[i];
      conf[i] += (ranf()-0.5)*dx;
      cfsv[N+i] = conf[N+i];
      conf[N+i] += (ranf()-0.5)*dy;
      cfsv[2*N+i] = conf[2*N+i];
      conf[2*N+i] += (ranf()-0.5)*dz;
    }

    double wold = w;
    wave();
    ++ia;


    if (Math.exp(w-wold)<ranf()) {
      for (int i=0; i<nv; ++i) conf[i] = cfsv[i];
      w = wold;
      --ia;
    }
  }



  public static void move() {
    int im[] = new int[ne];
    double xg[] = new double[2];
    double gn[] = new double[nv];
    double cfsv[] = new double[nv];
    double enew[] = new double[ne];
    double weight[] = new double[ne];
    double ensv[][] = new double[nv][ne];
    double ebar = 0;
    double ekav = 0;
    double wbar = 0;
    for (int i=0; i<np; ++i) {
      for (int j=0; j<nv; ++j) conf[j] = cfsv[j] = ensm[j][i];


      drift();
      for (int j=0; j<nv-1; j+=2){
        xg = rang();
        gn[j] = xg[0];
        gn[j+1] = xg[1];
      }
      for (int j=0; j<nv; ++j) conf[j] += vd[j]*dt+gn[j]*st;


      energy();
      enew[i] = e;
      weight[i] = Math.exp((er-enew[i])*dt);
      ebar += weight[i]*e;
      ekav += weight[i]*ek;
      wbar += weight[i];
      for (int j=0; j<nv; ++j) ensv[j][i] = conf[j];
    }
    e = ebar/wbar;
    ek = ekav/wbar;


    double factor = (ne-dn)/wbar;
    int ix = 0;
    for (int i=0; i<np; ++i){
      im[i] = (int)(weight[i]*factor+ranf());
      for (int l=0; l<im[i]; ++l){
        for (int j=0; j<nv; ++j) ensm[j][ix] = ensv[j][i];
        eold[ix] = enew[i];
        ++ix;
      }
    }
    np = ix;
    er += ka*Math.log(wbar/ws);
    ws = wbar;
    ++step;
  }


  public static void pair() {
  double rij = 0;

    for (int i=0; i<N; ++i) {
      x[i] = conf[i];
      y[i] = conf[N+i];
      z[i] = conf[2*N+i];
    }

    int bin = 0;
    for (int i=0; i<N-1; ++i) {
      for (int j=i+1; j<N; ++j) {
        xx[i][j] = x[i]-x[j];
        yy[i][j] = y[i]-y[j];
        zz[i][j] = z[i]-z[j];
        if (ig == 3)
          rij = Math.sqrt(xx[i][j]*xx[i][j]+yy[i][j]*yy[i][j]
                    +zz[i][j]*zz[i][j]);
        else if (ig == 2)
          rij = Math.sqrt(xx[i][j]*xx[i][j]+yy[i][j]*yy[i][j]);
        bin = (int)(rij/dr);
        if (bin < bmax) hist[bin] += 2;
      }
    }
  }


  public void save() {
    boolean bfile = false;
    try {
      File newdata = new File("data.new");
      File olddata = new File("data.old");
      if(newdata.exists()) bfile = newdata.renameTo(olddata);
      if(!newdata.exists()) bfile = newdata.createNewFile();
      DataOutputStream w = new DataOutputStream
          (new FileOutputStream("data.new"));
        w.writeInt(np);
        w.writeInt(seed);
        w.writeInt(step);
        w.writeDouble(er);
        for (int i=0; i<np; ++i) {
          w.writeDouble(eold[i]);
          for (int j=0; j<nv; ++j)
            w.writeDouble(ensm[j][i]);
        }
      }
      catch (Exception e) {
        System.out.println("Exception error: " + e.toString());
    }
  }


  public static void auto() {
    int kmax = nc/5;
    for (int k=0; k<kmax; ++k) {
      double sum11 = 0;
      double sum12 = 0;
      double sum21 = 0;
      double sum22 = 0;
      double sum30 = 0;
      for (int i=0; i<nc-k; ++i) {
        sum11 +=ecrl[i];
        sum12 +=ecrl[i+k];
        sum21 +=ecrl[i]*ecrl[i];
        sum22 +=ecrl[i+k]*ecrl[i+k];
        sum30 +=ecrl[i]*ecrl[i+k];
      }
      double corr = (nc-k)*sum30-sum11*sum12;
      corr /= Math.sqrt((nc-k)*sum21-sum11*sum11);
      corr /= Math.sqrt((nc-k)*sum22-sum12*sum12);
      System.out.println("K = " + k + "autocorrelation function =" + corr);
    }
  }


  public static double ranf() {
    final int a = 16807, c = 2147483647, q = 127773,
      r = 2836;
    final double cd = c;
    int h = seed/q;
    int l = seed%q;
    int t = a*l-r*h;
    if (t > 0) seed = t;
    else seed = c+t;
    return seed/cd;
  }


  public static double[] rang() {
    double x[] = new double[2];
    double r1, r2;
    r1 = - Math.log(1-ranf());
    r2 = 2*Math.PI*ranf();
    r1 = Math.sqrt(2*r1);
    x[0] = r1*Math.cos(r2);
    x[1] = r1*Math.sin(r2);
    return x;
  }



  public static double xe(double t) {
    return Math.exp(-t);
  }

  public static double u(double t) {
    return Math.pow(a/t,2.5)+b/(c+t);
  }

  public static double u1(double t) {
    return -5*Math.pow(a/t,2.5)/t-2*b/((c+t)*(c+t));
  }

  public static double u2(double t) {
    return 30*Math.pow(a/t,2.5)/t+8*b*t/((c+t)*(c+t)*(c+t))
      - 2*b/((c+t)*(c+t));
  }

  public static double up(double t) {
    return u2(t)+2*u1(t);
  }

  public static double v(double t) {
    return v0*Math.exp(-a0*t-b0*t*t)
          -f(t)*(c6/Math.pow(t,6)+c8/Math.pow(t,8)
          +c10/Math.pow(t,10));
  }

  public static double f(double t) {
    if (t>=d)
      return 1;
    else {
      double ex = d/t-1;
      ex *= ex;
      return Math.exp(-ex);
    }
  }
  public static double ue(double t) {
    return u0*Math.exp(-g0*t)
          -a3/Math.pow(t,3)-a4/Math.pow(t,4);
  }
}
