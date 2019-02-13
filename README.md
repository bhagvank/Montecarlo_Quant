# Montecarlo_Quant
Monte carlo simulations  in finance industry using quantum computer
Monte Carlo Quant is an open source project initiated by researchers at Quantica to lay the foundations for a platform which nurtures the development of quantum computation packages aimed at the speed up of simulations in computational finance. 


![alt text](https://github.com/bhagvank/arc/blob/master/monte_carlo_quant.png)

# [MonteCarlo Demo](https://guarded-garden-28229.herokuapp.com/montecarlo/)

# Use Cases

1.options pricing

2.return predictions.

3.portfolio evaluation.

4.personal financial planning.

5.capital investment impact.

# Monte Carlo Simulations

Monte Carlo is a family of numerical simulation technique extensively used in statistical physics and mathematical finance. It has historically proven records of being one of the most reliable technique addressing vital problems in industry and academics. A Monte Carlo simulation predicts the outcome of functions which are inherently indeterministic. A system simulated with MC performs a random walk within all possible states it can evolve. An ensemble average determined from all the states gives an estimate of the mathematical quantity of interest. 
# Natural Algorithm
Now let's quickly peep in the general procedure for an MC simulation. A natural algorithm is executed n times to generate n number of random samples. The probability of finding the average value of a state variable within a specified range of tolerance scales directly with the square of variance and inversely with the square of tolerance and number of samples. The value of n is kept sufficiently large enough to obtain a probability above 99%. 
The algorithm is computationally expensive because of the quadratic dependence of probability on the number of trials O(N2). The fact has severely limited it's application to small-scale systems with few data sets. Recently researchers have come across ways to speed up the MC simulation with the use of quantum algorithms. Quantum algorithms are analogous to classical algorithms expect that their design is to find implementation on a quantum architecture. It poses several advantages over its classical counterpart, imparting enhanced speed and realistic randomness. Quantum walks are a quantum analogue to random walks and have substantially reduced the time-consumption in MC simulations for mixing of Markov chains as reported by Ashley Montanaro(2015). Several algorithms are developed so far to encompass the need to accelerate the classical deterministic and randomised algorithms with rigorous performance bounds — the error rates of outcome benchmark the effectiveness of these algorithms. 
The line of research on performing quantum integration algorithms started from Abrams and Williams (1999), who established the foregrounds which finally helped in the development of parallel computation of high dimensional integrals by Heinrich et al. (2001). 
The measurement on an n qubit system with all the qubits initialised to state |0>...|0> operated by a Walsh-Hadamard transform gives a random outcome |i> with probability 2-n. Thus, a quantum computer implements Monte Carlo with real physical randomness. Now lets define a quantum query given by a unitary map

Qf: |i>|y>→|i>|y + f(i)> (0 ≤ i < 2n-1)

If we set the last qubit as zero, then Qf acts as a subroutine which will update the last entry with function output. In an MC simulation, the f(i) can be the integration function.
A practical implementation of some of these algorithms on a hypothetical quantum computer and its betterment is the original motto of this project. We specifically focus on MC pricing of financial derivatives and how the arrangement of probability distributions can leverage the advantages of quantum superposition and thus accelerate the simulation. The prices of derivatives are extracted through quantum measurements with high confidence. We expect a quadratic speed up as reported in recent literature (Patrick Rebentrost, (2018)). 

After the financial crisis of 2008-2009, it's essential for the government and banks to estimate the risk exposure of the financial institutions. Such risks follow the nomenclature XVA where X stands for the type of risk associated and VA for value adjustment.  Different MC simulations performed overnight assesses the risk under different scenarios. Reducing the time-consumption of these overnight calculations facilitates the institutions to take actions following the changing market conditions. 


## Prerequisites for classical python

1. Ensure that  python3 is installed.
  * [Python3](https://www.python.org/downloads/)
  
2. Git clone this repository
```
git clone https://github.com/bhagvank/Montecarlo_Quant.git

```
3. Pipenv install : install the packages required
```
pipenv install
```

4. Pipenv shell : Run the virtual env 
```
pipenv shell
```
5. Run helloworld and estimator examples
```
python monteCarlo.py
python estimator.py

```
6. Exit the shell
```
exit
```

# Instructions for setting up locally - classical/webapp
1. Ensure that mysql is installed, python3 and django for polls app - mysite folder.

  * [Python3](https://www.python.org/downloads/)

  * [Django](https://docs.djangoproject.com/en/2.0/topics/install/#installing-official-release)

  * [MySql](https://www.mysql.com/downloads/)
  
  
2.git clone this repository
```
git clone https://github.com/bhagvank/Montecarlo_Quant.git

```
   
3. Pipenv install : install the packages required
```
pipenv install
```

4. Pipenv shell : Run the virtual env 
```
pipenv shell
```  
   
5. run locally using settings (update the database username and password)
```
python3 manage.py migrate --settings=mysite.run_settings
python3 manage.py runserver --settings=mysite.run_settings

```

6. Exit the shell
```
exit
```  
