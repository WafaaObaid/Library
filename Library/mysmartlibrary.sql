-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 05, 2025 at 04:01 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mysmartlibrary`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `status` enum('available','borrowed') NOT NULL,
  `number_of_sales` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `author`, `status`, `number_of_sales`) VALUES
(1, 'Pride and Prejudice', 'Jane Austen', 'available', 140),
(2, ' Little Women', 'Louisa May Alcott', 'borrowed', 100),
(3, 'Harry Potter and the Philosopher\'s Stone', 'J. K. Rowling', 'borrowed', 120),
(4, ' Howl\'s Moving Castle', 'Diana Wynne Jones', 'available', 95),
(5, 'The Twilight Saga', 'Stephenie Meyer', 'available', 80),
(6, 'It Ends with Us', 'Colleen Hoover', 'borrowed', 150),
(7, 'Ababil', 'Ahmed Al Hamdan', 'available', 125),
(8, 'Les Misérables', 'Victor Hugo', 'borrowed', 160),
(9, 'The Night Circus', 'Erin Morgenstern', 'available', 135),
(10, 'Oliver Twist', 'Charles Dickens', 'borrowed', 170);

-- --------------------------------------------------------

--
-- Table structure for table `borrowing`
--

CREATE TABLE `borrowing` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  `borrow_date` date NOT NULL,
  `return_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowing`
--

INSERT INTO `borrowing` (`id`, `book_id`, `member_id`, `borrow_date`, `return_date`) VALUES
(1, 1, 1, '2025-09-14', '2025-09-29'),
(2, 2, 4, '2025-09-09', '2025-09-13'),
(3, 3, 3, '2025-09-18', '2025-09-25'),
(4, 4, 3, '2025-09-17', '2025-09-24'),
(5, 5, 2, '2025-09-18', '2025-09-26'),
(6, 6, 3, '2025-09-18', '2025-09-30'),
(7, 7, 4, '2025-09-16', '2025-09-28'),
(8, 8, 2, '2025-09-18', '2025-09-23'),
(9, 9, 1, '2025-09-15', '2025-09-26'),
(10, 10, 1, '2025-09-17', '2025-09-27'),
(11, 4, 2, '2025-09-18', NULL),
(12, 6, 3, '2025-09-24', NULL),
(13, 10, 2, '2025-09-25', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`id`, `name`, `contact`) VALUES
(1, 'natali', 59933333),
(2, 'wafaa', 5626666),
(3, 'sami', 592222),
(4, 'Ali', 56378888);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `password_hash`) VALUES
(1, 'Ahmad', 'Ali', 'ahmad@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b'),
(2, 'Wafaa', 'Obaid', 'wafaa@gmail.com', '5f4dcc3b5aa765d61d8327deb882cf99'),
(3, 'Nataly', 'Abushbak', 'nataly@gmail.com', '202cb962ac59075b964b07152d234b70'),
(4, 'Ali', 'Sami', 'ali@gmail.com', '984d8144fa08bfc637d2825463e184fa'),
(5, 'sara', 'waleed', 'sara@gmail.com', '312dc6ec7c900fb9017bf43c6b1f81bb'),
(6, 'Mohammed', 'Omar', 'mohammed@gmail.com', '725b54dd388a13cc059e15daa9d00fdc'),
(7, 'wassem', 'mohammed', 'wassem@gmail.com', '202cb962ac59075b964b07152d234b70'),
(8, 'marwa', 'omar', 'marwa@gmail.com', '202cb962ac59075b964b07152d234b70');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `borrowing`
--
ALTER TABLE `borrowing`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `member_id` (`member_id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `borrowing`
--
ALTER TABLE `borrowing`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `members`
--
ALTER TABLE `members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `borrowing`
--
ALTER TABLE `borrowing`
  ADD CONSTRAINT `borrowing_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  ADD CONSTRAINT `borrowing_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
