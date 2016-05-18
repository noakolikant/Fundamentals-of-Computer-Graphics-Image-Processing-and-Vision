function [ inliers, M ] = homdistfn(M, x, t)
%distfn 
    x1 = x(1:3, :);
    x2 = x(4:6, :);
    Mx1 = M*x1;

    for i=1:size(x, 2)
        x1(:, i) = x1(:, i) ./ x1(3, i);
        x2(:, i) = x2(:, i) ./ x2(3, i);
        Mx1(:, i) = Mx1(:, i) ./ Mx1(3, i);
     end

    d2 = sum((x2-Mx1).^2);
    inliers = find(abs(d2) < t);

end

